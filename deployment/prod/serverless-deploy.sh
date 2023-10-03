export STAGE=prod
export PROFILE=prod

pushd ../

./serverless-deploy.sh

popd