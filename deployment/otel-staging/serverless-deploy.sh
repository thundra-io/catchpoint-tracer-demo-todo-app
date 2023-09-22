export STAGE=staging
export PROFILE=otel-staging

pushd ../

./serverless-deploy.sh

popd