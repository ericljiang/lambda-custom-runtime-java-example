FROM amazonlinux
RUN yum install -y which tar gzip gcc glibc-devel zlib-devel
RUN amazon-linux-extras install java-openjdk11
RUN export JAVA_HOME=$(readlink -f `which java`)
