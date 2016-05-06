# 懒癌卫士(Pcb)
懒癌卫士(Pcb)，是一个1.9的命令编辑器。  
## 功能:
+ 把特殊格式(pcb格式)的命令转为OOC(如果命令过长则可能为多条OOC)
+ 高亮Pcb语法
+ 区块折叠
+ 实用编辑功能(转义、插入§、颜色黑科技、插入随机UUID Least/Most等)
+ 自动补全命令参数
+ 穷举助手
+ 部分命令生成器(如颜色NBT生成、UUID互换、HideFlag生成、DisableSlot生成、可视化JSON编辑等)

## 目前进度:
刚从C#版进入java版重构，目前完成了Pcb -> OOC的部分。  
TODO List:
+ autocomplete-data -> tree, 供自动补全使用
+ 编辑器GUI

## Compile:
    gradle build

## 使用教学:
[Wiki](../../wiki)
