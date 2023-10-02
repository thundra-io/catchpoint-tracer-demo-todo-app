export STAGE=qa
export PROFILE=otel-qa

pushd ../

./serverless-deploy.sh

popd