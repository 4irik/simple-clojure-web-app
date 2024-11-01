#!/bin/bash

# Будем раскрашивать сообщения чтобы можно было не читая понять что происходит
COLOR_FAIL='\033[0;31m' # Red
COLOR_SUCCESS='\033[0;32m' # Green
COLOR_NC='\033[0m' # No Color

# смотрим чтобы не было модифицированных или не отслеживаемых файлов
# для этого получим списпок файлов и посчитаем их кол-во
git_files_count=$(git status -s | wc -l)
if [ $git_files_count -ne 0 ]; then
  echo -e "${COLOR_FAIL}FAIL:${COLOR_NC}"
  echo "Проект имеет неотслеживаемые или модифицированные файлы в кол-ве ($git_files_count штук)"
  exit 1
fi

# получим короткий хэш коммита
hash=$(git rev-parse --short HEAD)

# запустим сборку
build_name="patient:$hash"
docker build --build-arg version=$hash -t $build_name -f ./build/Dockerfile .

if [ $? -ne 0 ]; then
  echo -e "${COLOR_FAIL}FAIL:${COLOR_NC}"
  echo "Сборка завершилась с ошибками, см. сообзения выше."
  exit 1
fi

echo -e "${COLOR_SUCCESS}SUCCESS:${COLOR_NC} $build_name"
exit 0
