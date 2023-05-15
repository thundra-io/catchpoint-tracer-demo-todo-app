pushd ..
mvn clean package -DskipTests

popd
pushd app
npm install


cdk bootstrap --no-color
cdk deploy --all --require-approval never --no-color

popd