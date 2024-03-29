<div align="center">
   <img width="160" src="https://github.com/mamoe/mirai/blob/dev/docs/mirai.png" alt="logo"></br>


   <img width="95" src="https://github.com/mamoe/mirai/blob/dev/docs/mirai.svg" alt="title">

----
Mirai 是一个在全平台下运行，提供 QQ 协议支持的高效率机器人库

这个项目的名字来源于
<p><a href = "http://www.kyotoanimation.co.jp/">京都动画</a>作品<a href = "https://zh.moegirl.org/zh-hans/%E5%A2%83%E7%95%8C%E7%9A%84%E5%BD%BC%E6%96%B9">《境界的彼方》</a>的<a href = "https://zh.moegirl.org/zh-hans/%E6%A0%97%E5%B1%B1%E6%9C%AA%E6%9D%A5">栗山未来(Kuriyama <b>Mirai</b>)</a></p>
<p><a href = "https://www.crypton.co.jp/">CRYPTON</a>以<a href = "https://www.crypton.co.jp/miku_eng">初音未来</a>为代表的创作与活动<a href = "https://magicalmirai.com/2019/index_en.html">(Magical <b>Mirai</b>)</a></p>
图标以及形象由画师<a href = "https://github.com/DazeCake">DazeCake</a>绘制
</div>

# mirai-compose

[![GitHub release (latest SemVer including pre-release)](https://img.shields.io/github/v/release/sonder-joker/mirai-compose?include_prereleases)](https://github.com/sonder-joker/mirai-compose/releases)
![QQ Group](https://img.shields.io/badge/交流群-1004268447-informational?style=flat-square&logo=tencent-qq)
[![MiraiForum](https://img.shields.io/badge/官方论坛-mirai--forum-blueviolet?style=flat-square&logo=appveyor)](https://mirai.mamoe.net)

## 这是什么？

这是一个基于[compose-jb](https://github.com/jetbrains/compose-jb) ，实现的[mirai-console](https://github.com/mamoe/mirai-console)
前端。

## 他稳定吗？

目前还是属于不稳定但能够使用的阶段，欢迎各位来提issue和feature。

## 如何下载？

可以在[右侧的release页面](https://github.com/sonder-joker/mirai-compose/releases) 下载符合当前操作系统的版本。
同样拥有国内镜像，在 [mirai-forum论坛的帖子内](https://mirai.mamoe.net/topic/215/mirai-compose-%E8%B7%A8%E5%B9%B3%E5%8F%B0-%E5%9B%BE%E5%BD%A2%E5%8C%96-%E6%98%93%E5%AE%89%E8%A3%85%E7%9A%84mirai-console%E5%AE%A2%E6%88%B7%E7%AB%AF)
可以下载

## 如何使用？

直接安装即可，功能详见[文档](docs/FEATURES.md)。

## 局限

由于compose-jb并不提供32位环境，使用32位的MiraiNative会造成意想不到的结果。
