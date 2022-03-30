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
* 2/28
    * 在教务处网站确认任务书并提交开题报告
    * translation
## 3 月
* 3/1
    * translation
* 3/2
    * 完成 ``VANE`` 的第一遍精读，完成初步翻译
* 3/7
    * 提问：关于数据集输入归一化和矩阵化
    * 寻找线性回归相关代码
* 3/8
    * 导入红酒数据集，学习 csv 格式文件的读取
* 3/9
    * 成功读取 csv 格式的数据（测试）
    * 读取工具类设计
* 3/10
    * 编写读取工具类
* 3/11
    * 实现提取特征向量和目标变量的分离
    * 实现计算特征向量的最值
* 3/13
    * 整体设计结构改进（已有思路，尚未实现）
* 3/14
    * 整体设计结构改进实现（初步实现各司其职）
* 3/15
    * DataNormalization 实现
    * ~~问题：TA 加随机数的时候取整没实现~~
* 3/16
    * ``VANE`` 的 4.3.1 SystemInitialization 实现
    * 设计本地数据矩阵计算
* 3/17
    * 小重构
* 3/18
    * DO 部分 dataPreprocessing 实现（附带有单独测试用例并通过 & 复杂度尚未测试，可能可以进一步优化）
* 3/19
    * ``VANE`` 的 4.3.2 Local training data generation and encryption 实现
* 3/20
    * ``VANE`` 的 4.3.3 Local training data aggregation 的 step1 实现（既隐私保护加解密全部实现）
* 3/21
    * 测试隐私保护加解密（bug 待修改）
* 3/22
    * 查阅线性回归相关资料
    * 修 bug（paillier算法加解密异常，时常发生解密错误的情况，尚未发现原因）
* 3/23
    * 修复 paillier算法加解密的异常，原因是：
        * 解密时 mod N mod γ，如果 x 是一个介于γ和N之间的数，恢复出来的就不对
    * 优化测试的输出显示
* 3/24
    * 编写线性回归工具类
* 3/25
    * 提问学长线性回归相关问题
        1. 机器学习是个调参的过程，需要根据实际数据调试学习率
            * 可以通过输出损失函数进行判断，如果每轮之间变化较小说明学习率设置偏小；如果变化很大说明过拟合
        2. ``VANE`` 文中聚合只有一个数据，这种情形下的训练效果和初始值有很大的关系
            * 线性回归就是找出一条能穿过尽量多的数据点的直线，初始值无论设置什么，随着训练都会趋于一条最终确定的直线
            * 但是聚合之后把所有点加起来变成了一个点，穿过一个点的直线就有无数条
            * 聚合完之后，矩阵里的每个值都很大，如果用明文数据训练得到的学习率可能就不适用了 
        3. 建议尝试：
            1. 先不聚合，对明文正常训练，确定下来学习率之后再调试初始值。
            2. 聚合，固定初始值令其全为1，调试学习率。 
* 3/26
    * 明文训练调参
* 3/27
    * 明文训练调参（损失函数降到6）
* 3/28
    * 学习率动态调整模块学习和编写
    * 试用 commons-math3 ，训练结果正确率只有 60% 
* 3/30
    * 明文线性回归训练（迭代 500 次，用时 1h 20min，正确率 43%）