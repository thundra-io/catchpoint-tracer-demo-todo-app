export STAGE=prod
export PROFILE=otel-prod

pushd ../

./serverless-deploy.sh

popd