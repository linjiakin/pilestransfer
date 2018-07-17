## 1.判断在线指令

​	好易充：tradeTypeCode=1  循道=2

http://59.110.170.111:80/piles-test-web-1.0.0/tool/connection?tradeTypeCode=1&pileNo=0000000080000232

## 2.升级指令

好易充：

tail -200f piles.log |grep -a '向蔚景' |grep '68 1e'  如果要针对电桩查询 则 再grep '${电桩ip}'----push

tail -200f piles.log |grep -a  '68 9e'  |grep '${电桩ip}'  查询升级命令回复

循道：

tail -200f piles.log |grep -a '向循道' |grep '68 b8'  如果要针对电桩查询 则 再grep '${电桩ip}'----push

tail -200f piles.log |grep -a  '68 14'  |grep '${电桩ip}'  查询升级命令回复

## 3.查看升级状态（升级成功与否）

好易充：

tail -200f piles.log |grep -a '蔚景充电桩升级结果汇报报文'  |grep '${桩编号}'

循道：

tail -200f piles.log |grep -a '循道充电桩升级结果汇报报文'   |grep '${桩编号}'

## 4.查看设备超时

tail -200f piles.log |grep -a   ‘TIME_OUT’   ---push命令超时查询

## 5.查看设备心跳

好易充：

tail -200f piles.log |grep -a '接收到充电桩心跳报文'  |grep '${桩编号}'

循道：

tail -200f piles.log |grep -a '电桩ip'  |grep '68 4 83 '

## 6.查看设备何时断线

倒序查询日志，先查询对应充电桩编号

cat piles.log |grep -a '${桩编号}' 可以查到对应的ip

然后 倒序查询 对应ip所出现的时间

cat piles.log |grep -a ’${ip}‘ >>1.txt 然后看1.txt最后的信息就行了

## 7.查看设备充电状态

同10

## 8.查看设备充电结果

好易充：

tail -200f piles.log |grep -a '接收到充电桩上传充电记录报文'  |grep '${桩编号}'

循道：

tail -200f piles.log |grep -a '接收到循道充电桩上传充电记录报文'   |grep '${桩编号}'

## 9.查看充电结果汇报

tail -200f piles.log |grep -a '请求信息'  |grep '${桩编号}' |grep -a '返回结果' 

## 10.查看设备状态。

好易充：

tail -200f piles.log |grep -a '接收到充电桩心跳报文'  |grep '${桩编号}'

循道：

tail -200f piles.log |grep -a '接收到循道充电桩上传充电过程监测数据报文'   |grep '${桩编号}'