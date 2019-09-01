FROM postgres:11

RUN localedef -i zh_CN -c -f UTF-8 -A /usr/share/locale/locale.alias zh_CN.UTF-8 && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENV POSTGRES_PASSWORD Postgres.2019
ENV LANG="zh_CN.UTF_8"

COPY docs/init.sql /docker-entrypoint-initdb.d/
