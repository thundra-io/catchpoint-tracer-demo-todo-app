import * as cdk from 'aws-cdk-lib';
import * as elasticbeanstalk from 'aws-cdk-lib/aws-elasticbeanstalk';
import * as s3assets from 'aws-cdk-lib/aws-s3-assets';
import * as iam from 'aws-cdk-lib/aws-iam';
import { Construct } from 'constructs';

import * as path from 'path';

const APP_NAME = 'cp-tracer-demo-todo-app';

export class DeployStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    const role = new iam.Role(this, `${APP_NAME}-role`, {
      assumedBy: new iam.ServicePrincipal('ec2.amazonaws.com'),
    });

    const managedPolicy = iam.ManagedPolicy.fromAwsManagedPolicyName('AWSElasticBeanstalkWebTier');
    role.addManagedPolicy(managedPolicy);

    const instanceProfile = new iam.CfnInstanceProfile(this, `${APP_NAME}-instance-profile`, {
      roles: [role.roleName],
      instanceProfileName: `${APP_NAME}-instance-profile`,
    });

    const application = new elasticbeanstalk.CfnApplication(this, APP_NAME, {
      applicationName: APP_NAME,
    });

    const appZip = new s3assets.Asset(this, `${APP_NAME}-zipped`, {
      path: path.join(__dirname, '../../../target/todo-app.zip'),
    });

    const version = new elasticbeanstalk.CfnApplicationVersion(this, `${APP_NAME}-version`, {
      applicationName: application.applicationName || APP_NAME,
      sourceBundle: {
        s3Bucket: appZip.s3BucketName,
        s3Key: appZip.s3ObjectKey,
      },
    });

    const options: elasticbeanstalk.CfnEnvironment.OptionSettingProperty[] = [
      {
        namespace: 'aws:autoscaling:launchconfiguration',
        optionName: 'IamInstanceProfile',
        value: instanceProfile.instanceProfileName || `${APP_NAME}-instance-profile`,
      },
      {
        namespace: 'aws:autoscaling:launchconfiguration',
        optionName: 'InstanceType',
        value: 't3.micro',
      },
    ];

    const environment = new elasticbeanstalk.CfnEnvironment(this, `${APP_NAME}-env`, {
      environmentName: `${APP_NAME}-env`,
      applicationName: application.applicationName || APP_NAME,
      solutionStackName: '64bit Amazon Linux 2 v3.4.7 running Corretto 8',
      optionSettings: options,
      versionLabel: version.ref,
    });

    version.addDependency(application);
    environment.addDependency(version);

    new cdk.CfnOutput(this, `${APP_NAME}-url`, {
      value: environment.attrEndpointUrl,
    });
  }
}
