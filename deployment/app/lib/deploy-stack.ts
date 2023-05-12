import * as cdk from 'aws-cdk-lib';
import * as elasticbeanstalk from 'aws-cdk-lib/aws-elasticbeanstalk';
import * as s3assets from 'aws-cdk-lib/aws-s3-assets';
import { Construct } from 'constructs';

import * as path from 'path';

const APP_NAME = 'catchpoint-tracer-demo-todo-app';

export class DeployStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

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
        value: 'aws-elasticbeanstalk-ec2-role',
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
      solutionStackName: '64bit Amazon Linux 2 v5.4.5 running Corretto 11',
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
