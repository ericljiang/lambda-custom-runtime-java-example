docker build -t amazonlinux-graalvm - < build.Dockerfile
docker run -i -v $(pwd):/source -w /source amazonlinux-graalvm ./gradlew buildZip --console=plain
