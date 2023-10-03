pushd ..
mvn clean package -DskipTests -Pbuild-serverless
popd

sls deploy --verbose
