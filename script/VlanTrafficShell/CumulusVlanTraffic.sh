#!/bin/bash
##
## Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
##

RemoteHost=$1
PW=$2
Prompt="\[#$%>\]"

VNIINT="VN Interface"
INOCT="Total In Octets"
OUTOCT="Total Out Octets"

isVNI=false
ifnameTag="{\"ifName\":"
inTag="\",\"inOctets\":"
outTag="\",\"outOctets\":"

ifname=""
inOctets=""
outOctets=""
json=""
array=()

logFile=$(cut -d'@' -f 2 <<<${RemoteHost})
ssh-keygen -R ${logFile} > /dev/null

function get_traffic(){
expect -c "
set timeout 20

spawn ssh -tt -o StrictHostKeyChecking=no ${RemoteHost}
expect {
    \"(yes/no)?\" {
    log_user 0
    send \"yes\n\"
    }
    \"password:\" {
        log_user 0
        send \"${PW}\n\"
    }
    timeout { exit 2 }
}

expect {
      \"denied\" {
       exit 1 
       }
    }
expect {
         -glob \"${Prompt}\" {
         log_file -noappend $logFile
         log_user 1
            send \"cat /cumulus/switchd/run/stats/vxlan/all\n\"
            expect eof 
        }
     }
   

expect {
        -regexp \"\n.*\r\" {
            log_user 0
            sleep 2
            send \"exit\n\"
            exit 0
        }
    }
"
ret=$?
if [ $ret -eq 1 ];then
 return 1
fi
if [ $ret -eq 2 ];then
 return 2
fi
return 0
}

RESULT=`get_traffic`
ret=$?
if [ $ret -eq 1 ];then
 echo "SSH connection error"
 exit 1
fi

if [ $ret -eq 2 ];then
 echo "TimeOut Error"
 exit 2
fi

readFile="./${logFile}"
count=0
echo "["
while read line
do
  target=${line%:*}
  if [[ $target = *$VNIINT* ]]; then
    isVNI=true
    vlan=${line##*:}
    ifname=`echo ${vlan##*,}`
  fi
  
  if [[ $isVNI == true ]]; then
    if [[ $target = *$INOCT* ]]; then
        inOctets=`echo ${line##*:}`
    fi
    if [[ $target = *$OUTOCT* ]]; then
        outOctets=`echo ${line##*:}`
        if [ $count -gt 0 ];then
            echo ","
        fi
        echo $ifnameTag"\""$ifname
        echo $inTag"\""$inOctets
        echo $outTag"\""$outOctets
        echo "\"}"
        count=`expr $count + 1`
        isVNI=false
    fi
  fi
done < $readFile
echo "]"
rmCommand="rm -f ${logFile}"
eval $rmCommand
exit 0
