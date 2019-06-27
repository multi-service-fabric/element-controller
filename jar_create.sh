#!/bin/sh
# Copyright(c) 2019 Nippon Telegraph and Telephone Corporation

## create EcMainModule.jar from src/ and lib/

## directory path of src/
EC_SRC_DIR=./msf

## cd
cd `dirname "$0"`

## delete class files
CLASS_FILE_LIST=`find ${EC_SRC_DIR} -type f | grep ".class"`
if [ ${#CLASS_FILE_LIST} -ne 0 ]; then
    rm -f ${CLASS_FILE_LIST}
fi

## get the list of java files
FILE_LIST=`find ${EC_SRC_DIR} -type f | grep ".java"`

for file in ${FILE_LIST}
do
    # delete BOM
    sed -i -s -e '1s/^\xef\xbb\xbf//' ${file}
done

## create class files
echo "java compile start..."
javac -encoding UTF-8 -cp .:lib/* -Xpkginfo:always ${FILE_LIST}

ret=$?

# make sure there are no errors
if [ ${ret} -eq 0 ]; then
    echo "java compile end."

    # create jar file
    # It is assumed that no error might be occurred due to diskfull,  
    # etc., by taking into account that jar file will be smaller than 
    # class file in terms of module's characteristics.
    echo "jar create start..."
    jar cvf EcMainModule.jar ${EC_SRC_DIR} >/dev/null
    echo "jar create end."
else
    echo "java compile FAILED."
fi
