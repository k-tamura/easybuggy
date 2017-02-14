EasyBuggy
=

EasyBuggyは、メモリリーク、デッドロック、JVMクラッシュ、SQLインジェクションなど、バグや脆弱性の動作を理解するためにつくられたバグだらけのWebアプリケーションです。

クイックスタート
-

    $ mvn clean install exec:exec

( または[JVMオプション](https://github.com/k-tamura/easybuggy/blob/master/pom.xml#L183)付きで ``` java -jar easybuggy.jar ``` か、任意のサーブレットコンテナに ROOT.war をデプロイ。 )

以下にアクセス:

    http://localhost:8080


停止するには:

  <kbd>CTRL</kbd>+<kbd>C</kbd>をクリック
  

詳細は
-
   
[wikiページ](https://github.com/k-tamura/easybuggy/wiki)を参照下さい。
