[![Build Status](https://travis-ci.org/umasuo/data-definition.svg?branch=master)](https://travis-ci.org/umasuo/data-definition)

# 数据格点定义(简称数据定义)
数据格点定义用于定义开发者在平台上需要使用到的数据格式，每当设备上传数据时均需要检查数据的格式是否正确，每当数据处理输出结果时均需检查数据格式是否正确。

所有的数据格点定义均采用json的方式来定义, 而所有的数据在存储进数据库之前，均需要进行数据格式的检查，如果格式不对，则不能够存储.

每个数据格点定义拥有以下特性：
- 全平台唯一ID
- 同一个开发者ID下，数据格点的自定义ID唯一
