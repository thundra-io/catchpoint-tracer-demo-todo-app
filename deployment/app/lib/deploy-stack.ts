import * as cdk from 'aws-cdk-lib';
import * as elasticbeanstalk from 'aws-cdk-lib/aws-elasticbeanstalk';
import * as s3assets from 'aws-cdk-lib/aws-s3-assets';
import * as sqs from 'aws-cdk-lib/aws-sqs';
import * as iam from 'aws-cdk-lib/aws-iam';
import { Construct } from 'constructs';

import * as path from 'path';

const APP_NAME = `cp-tracing-demo-todo-app`;
const PROFILE = process.env.PROFILE || 'staging';

export class DeployStack extends cdk.Stack {

  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    const notificationQueue = new sqs.Queue(this, `${APP_NAME}-notification-queue-${PROFILE}`, {
      queueName: `${APP_NAME}-notification-queue-${PROFILE}`,
    });

    const appPolicy = new iam.ManagedPolicy(this, `${APP_NAME}-policy-${PROFILE}`, {
      managedPolicyName: `${APP_NAME}-policy-${PROFILE}`,
      statements: [
        new iam.PolicyStatement({
          effect: iam.Effect.ALLOW,
          actions: [
            'sqs:*',
          ],
          resources: [
            notificationQueue.queueArn,
          ],
        }),
      ],
    });
    const managedPolicy =
        iam.ManagedPolicy.fromAwsManagedPolicyName('AWSElasticBeanstalkWebTier');

    const role = new iam.Role(this, `${APP_NAME}-role-${PROFILE}`, {
      assumedBy: new iam.ServicePrincipal('ec2.amazonaws.com'),
    });
    role.addManagedPolicy(appPolicy);
    role.addManagedPolicy(managedPolicy);

    const instanceProfile = new iam.CfnInstanceProfile(this, `${APP_NAME}-instance-profile-${PROFILE}`, {
      roles: [role.roleName],
      instanceProfileName: `${APP_NAME}-instance-profile-${PROFILE}`,
    });

    const application = new elasticbeanstalk.CfnApplication(this, `${APP_NAME}-${PROFILE}`, {
      applicationName: `${APP_NAME}-${PROFILE}`,
    });

    const appZip = new s3assets.Asset(this, `${APP_NAME}-artifact-${PROFILE}`, {
      path: path.join(__dirname, '../../../target/todo-app.zip'),
    });

    const version = new elasticbeanstalk.CfnApplicationVersion(
      this,
      `${APP_NAME}-version-${PROFILE}`,
      {
        applicationName: application.applicationName || `${APP_NAME}-${PROFILE}`,
        sourceBundle: {
          s3Bucket: appZip.s3BucketName,
          s3Key: appZip.s3ObjectKey,
        },
      }
    );

    const options: elasticbeanstalk.CfnEnvironment.OptionSettingProperty[] = [
      {
        namespace: 'aws:autoscaling:launchconfiguration',
        optionName: 'IamInstanceProfile',
        value: instanceProfile.instanceProfileName || `${APP_NAME}-instance-profile-${PROFILE}`,
      },
      {
        namespace: 'aws:autoscaling:launchconfiguration',
        optionName: 'InstanceType',
        value: 't3.small',
      },
      {
        namespace: 'aws:elasticbeanstalk:environment',
        optionName: 'EnvironmentType',
        value: 'SingleInstance',
      },
      {
        namespace: 'aws:elasticbeanstalk:cloudwatch:logs',
        optionName: 'StreamLogs',
        value: 'true',
      },
      {
        namespace: 'aws:elasticbeanstalk:application:environment',
        optionName: 'TODO_APP_NOTIFICATION_QUEUE_URL',
        value: notificationQueue.queueUrl,
      },
    ];

    const environment = new elasticbeanstalk.CfnEnvironment(this, `${APP_NAME}-env-${PROFILE}`, {
      environmentName: `${APP_NAME}-${PROFILE}`,
      applicationName: application.applicationName || `${APP_NAME}-${PROFILE}`,
      solutionStackName: '64bit Amazon Linux 2 v3.4.7 running Corretto 8',
      optionSettings: options,
      versionLabel: version.ref,
      cnamePrefix: `${APP_NAME}-${PROFILE}`,
    });

    version.addDependency(application);
    environment.addDependency(version);

    new cdk.CfnOutput(this, `${APP_NAME}-url-${PROFILE}`, {
      value: environment.attrEndpointUrl,
    });

  }
}
