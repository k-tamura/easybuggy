[![Build Status](https://travis-ci.org/k-tamura/easybuggy.svg?branch=master)](https://travis-ci.org/k-tamura/easybuggy)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![GitHub release](https://img.shields.io/github/release/k-tamura/easybuggy.svg)](https://github.com/k-tamura/easybuggy/releases/latest)

EasyBuggy :baby_symbol:
=

EasyBuggyは、[メモリリーク、デッドロック、JVMクラッシュ、SQLインジェクションなど](https://github.com/k-tamura/easybuggy/wiki)、バグや脆弱性の動作を理解するためにつくられたバグだらけのWebアプリケーションです。

![logo](https://github.com/k-tamura/easybuggy/blob/master/src/main/webapp/images/easybuggy.png)
![vuls](https://github.com/k-tamura/test/blob/master/bugs.png)

:clock4: クイックスタート
-

    $ mvn clean install

( または[JVMオプション](https://github.com/k-tamura/easybuggy/blob/master/pom.xml#L204)付きで ``` java -jar easybuggy.jar ``` か任意のサーブレットコンテナに ROOT.war をデプロイ。 )

以下にアクセス:

    http://localhost:8080


### 停止するには:

  <kbd>CTRL</kbd>+<kbd>C</kbd>をクリック ( または: http://localhost:8080/exit にアクセス )
  

:clock4: 詳細は
-
   
[wikiページ](https://github.com/k-tamura/easybuggy/wiki)を参照下さい。

使用例
-

EasyBuggyを起動して、無限ループ、LDAPインジェクション、UnsatisfiedLinkError、BufferOverflowException、デッドロック、メモリリーク、JVMクラッシュの順で実行しています。

![usage](https://github.com/k-tamura/test/blob/master/demo_eb_ja.gif)
