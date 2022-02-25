# 毕设记录

## 2 月
* 2/11
    * 构建仓库，保存参考论文
    * 学习 ``Paillier`` 密码体制
* 2/12
    * 修改加解密中有关``剩余系``的逻辑判断
    * 修改随机选取逻辑
    * **注：实际解密可能遇到负数问题，待解决**
* 2/13
    * ``getting M random numbers whose sum is N`` 算法第一版实现
    * 完成改进版 ``Paillier`` 加密的 ``Key Generation``
    * ~~**疑问：``Note that in Paillier cryptosystem, the order of g is a multiple of N, i.e., org(g) = kN, where k is an integer.``**~~
* 2/14
    * 添加工具类 ``BigIntegerUtils``
    * 编写改进版 ``Paillier`` 加密的 ``Data Encryption``、``Data Aggregation`` 和 ``Aggregated Result Decryption``（不能跑）
    * ~~**疑问：g 是某个循环群 G 的生成元，它的阶是 kN 的话，g^kN = 1。如何计算 k ？**~~
    * 学长建议：通常 ``Paillier`` 里面 g 取 n + 1 即可
* 2/15
    * 完成改进版 ``Paillier`` 加密（能跑）
    * ``Paillier`` 公钥中随机数 g 改为定值 N + 1 便于计算
* 2/16
    * 重构 ``SecureDataAggregationAlgorithm``，优化测试输出
* 2/17
    * 找参考论文，阅读开题报告和任务书模板
* 2/18
    * 开题报告 & 任务书初稿（不能交版）
* 2/19
    * 阅读加密算法相关文献
    * 开题报告 & 任务书修改（大概可以交）
* 2/23
    * 阅读 ``VANE`` 第四章，翻译符号定义
* 2/24
    * 根据批注修改开题报告 & 任务书（未发送）
* 2/25
    * 添加参考文献
    * 根据批注修改开题报告 & 任务书（已发送）