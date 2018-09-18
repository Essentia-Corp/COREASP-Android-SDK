# COREASP Android SDK

## 概要

COREASP Android SDK は、プッシュ通知ASPサービス「CORE ASP」の Android用のSDKになります。ドキュメントは CORE ASP Developer サイトに掲載しております。
 
■公式サイト

CORE ASP：<a href="http://core-asp.com">http://core-asp.com</a>

CORE ASP Developer（開発者向け）：<a href="http://developer.core-asp.com">http://developer.core-asp.com</a>


## 前提

* SDKは、Android4.1（Jelly Bean、APIレベル16）以上が動作対象になります。
* SDKは、FirebaseのFirebase Cloud Messaging（以下、FCM）のライブラリを使用します。
* SDKは、AARモジュールとして提供します。
* ProGuard設定は、SDKのAARモジュールの一部として提供されます。そのため、COREASP SDK自体に関するProGuard設定を追加する必要はありません。
* SDKを使用したアプリの統合開発環境は、Android Studioを前提に説明します。

## セットアップ

### アプリのAPIレベルの設定
------
本SDKは、Android4.1以上で動作するため、アプリ側のminSdkVersionに16以上を指定する必要があります。アプリ側のminSDKVersionを指定するには、以下の手順を実行します。

1.  アプリレベルの build.gradle(<プロジェクトルート>/<アプリモジュール>/build.gradle)を開き、defaultConfigブロックのminSdkVersionに16以上の値を設定します。

	```gradle
	android {
	    compileSdkVersion 26
	    buildToolsVersion "26.0.0"
	    defaultConfig {
	        ...
	        // 16以上の値を指定
	        minSdkVersion 16
	      	 ...
	    }
	}
	```

### SDKのAARモジュールの追加
------
ダウンロードしたSDKフォルダのcoreasp.aarのAARモジュールをプロジェクトに追加します。AARモジュールをプロジェクトに追加するには、以下の手順を実行します。

1. 以下の方法でAARモジュールをプロジェクトに追加します。
    1. [File] > [New Module] を選択します。
    2. [Import .JAR/.AAR Package]、[Next]の順に選択します。
    3. SDKフォルダのcoreasp.aarのファイルパスを入力し、[Finish]を選択します。
2. 以下の方法でアプリモジュールにAARモジュールの関連付けを行います。
    1. アプリモジュールを右クリックし、[Open Module Settings]を選択します。
    2. Dependenciesタブのリストの下にある[+]ボタンをクリックし、[Module Dependency]を選択します。
    3. 関連づけるAARモジュールをクリックし、[OK]ボタンを選択します。
3. settings.gradleファイルの先頭にライブラリが記載されていることを確認します。ライブラリがcoreaspの場合は、次のようになります。
	
	```gradle
	include ':app', ':coreasp'
	```
	
4.  アプリレベルの build.gradle(<プロジェクトルート>/<アプリモジュール>/build.gradle)を開き、dependenciesブロックに次のコードが記述されていることを確認します。

	```gradle
	dependencies {
	  ...
	  compile project(':coreasp')
	}
	```

### Firebaseのgoogle-services.jsonの追加
------
Firebaseの初期化に必要なgoogle-services.jsonの設定ファイルをプロジェクトに追加します。設定ファイルをプロジェクトに追加するには、以下の手順を実行します。

1. Firebase ConsoleでFirebaseのプロジェクトを作成します。プロジェクトを作成するには、以下の手順を実行します。
    1. [Firebase Console画面](https://console.firebase.google.com)にログインします。
    2. [プロジェクトを追加]ボタンを選択します。
    3. [プロジェクトを作成]画面で必要な項目を入力し、[プロジェクトを作成]を選択します。
    4. [AndroidアプリにFirebaseを追加]を選択します。
    5. [アプリの登録]画面でアプリプロジェクトのAndroidパッケージ名を入力し、[アプリの登録]を選択します。
    6. [設定ファイルのダウンロード]画面で[ダウンロード google-services.json]をクリックし、google-services.jsonをダウンロードします。
    7. google-services.jsonのダウンロード完了後、[続行]を選択します。
    8. [Firebase SDKの追加]画面の手順を確認し、[終了]を選択します。

2. ダウンロードしたgoogle-services.jsonをアプリモジュール配下に追加します。
3. ダウンロードしたgoogle-services.jsonを読み込むように、build.gradleファイルを修正します。
    1. プロジェクトレベルのbuild.gradle（<プロジェクトルート>/build.gradle）を変更します。
    
	    ```gradle
	    buildscript {
	        repositories {
	            jcenter()
	        }
	        dependencies {
	            classpath 'com.android.tools.build:gradle:2.3.3'
	            // 以下のを追加
	            classpath 'com.google.gms:google-services:3.1.0'
	        }
	    }
	    ```
    
    2. アプリレベルの build.gradle(<プロジェクトルート>/<アプリモジュール>/build.gradle)を変更します。

	    ```gradle
	    dependencies {
	    ...
	    }
	    
	    // 以下の行を追加
	    apply plugin: 'com.google.gms.google-services'
	    ```
    
    3. Android Studioの[Sync Now]をクリックし、gradleの設定を更新します。

### FCMの証明書設定の追加
------
CORE PUSHの通知送信に必要なFCMのサーバーキーをCORE ASPの管理画面に追加します。サーバーキーを追加するには、以下の手順を実行します。

1. FCMのサーバーキーを取得します。サーバーキーを取得するには、以下の手順を実行します。
    1. [Firebase Console画面](https://console.firebase.google.com)にログインします。
    2. [Firebaseのgoogle-services.jsonの追加]の手順で作成したFirebaseのプロジェクトを選択します。
    3. プロジェクトの[設定]を選択します。
    4. 設定画面の[クラウドメッセージング]タブをクリックし、プロジェクト認証情報に記載の[サーバーキー]の値をコピーします。（[以前のサーバーキー]の値でも使用可能ですが、[サーバーキー]の値をご使用ください）
2. FCMのサーバーキーを設定します。サーバーキーを設定するは以下の手順を実行します。
	1. [CORE ASP管理画面](http://core-asp.com/login.php)にログインします。
	2. [設定]をクリックし、Androidアプリを選択後、[設定画面へ移動]を選択します。
	3. [本番環境証明書]のテキスト欄にコピーしたサーバーキーの値を貼り付け、[設定内容を確認]を選択します。
	4. [設定内容を更新]を選択し、設定が更新されたことを確認します。

### SDKの依存ライブラリの追加
------
SDKの依存ライブラリを使用するように、build.gradleファイルを修正します。

1. アプリレベルの build.gradle(<プロジェクトルート>/<アプリモジュール>/build.gradle)を変更します。

	```gradle
	dependencies {
	    ...
	    // 以下の複数行を追加
	    compile 'com.android.support:appcompat-v7:26.+'
	    compile 'com.google.firebase:firebase-messaging:11.0.2'
	    compile 'com.google.android.gms:play-services-gcm:11.0.2'
	    ...
	    compile project(':coreasp')
	}
	
	apply plugin: 'com.google.gms.google-services'
	```
	
2.  Android Studioの[Sync Now]をクリックし、gradleの設定を更新します。

### アプリのマニフェストの必要な設定の追加
------

#### 設定キーの追加（必須）

CORE ASP管理画面にログインし、ホーム画面からAndroidアプリの設定キーを確認してください。 この設定キーをApplicationManifest.xmlのmeta-data要素のcom.coreasp.corepush.configKeyのandroid:value属性の値に指定します。
	
```xml
<!-- [必須] CORE PUSHの設定キー -->
<meta-data android:name="com.coreasp.corepush.configKey" android:value="@string/core_push_config_key"/>
```
meta-dataの読み込みに失敗した場合は、CoreAspManager#initialize呼び出し時にIllegalArgumentExceptionがスローされます。

#### 通知の起動アクティビティの追加（必須）

ステータスバーの通知をタップした後や通知ダイアログの表示をタップした後に起動するアクティビティのクラス名を
ApplicationManifest.xmlのmeta-data要素のcom.coreasp.corepush.notificationLaunchActivityのandroid:value属性の値に指定します。クラス名は絶対パス表記か相対パス表記で記載してください。

```xml
<!-- [必須] CORE PUSHの通知起動時のアクティビティ -->
<meta-data android:name="com.coreasp.corepush.notificationLaunchActivity" android:value=".MainActivity" />
```   

meta-dataの読み込みに失敗した場合は、CoreAspManager#initialize呼び出し時にIllegalArgumentExceptionがスローされます。

#### 通知アイコンのリソースの追加（必須）

通知時のステータスバーあるいは通知時のダイアログに表示されるアイコンのリソースIDをApplicationManifest.xmlのmeta-data要素のcom.coreasp.corepush.iconResourceIdのandroid:resource属性に指定します。ここで指定したアイコンリソースは、ステータスバーの通知アイコン(smallIcon)や通知ダイアログの通知アイコンに使用されます。

```xml
<!-- [必須] CORE PUSHの通知のアイコンリソースID -->
<meta-data android:name="com.coreasp.corepush.iconResourceId" android:resource="@mipmap/ic_launcher"/>
``` 

meta-dataの読み込みに失敗した場合は、CoreAspManager#initialize呼び出し時にIllegalArgumentExceptionがスローされます。

#### 通知アイコンのアクセントカラーの追加（任意）

Android5.0（Lollipop、APIレベル21）以上からマテリアルデザインに合わせて、ステータスバーの通知アイコン（smallIcon）は白で描画されるようになりました。
また、通知アイコン画像の背後にある円形のアクセントカラーを指定できるようになりました。このアクセントカラーを変更するには、ApplicationManifest.xmlのmeta-data要素のcom.coreasp.corepush.iconAccentColorのandroid:resource属性に色のリソースを指定します。
Android5.0未満では、以下の設定は無視されます。

```xml
<!-- CORE PUSHの通知のアイコンのアクセント色 (Android5.0以上で有効)-->
<meta-data android:name="com.coreasp.corepush.iconAccentColor" android:resource="@color/colorPrimary"/>
```

meta-dataの読み込みに失敗した場合は、アクセントカラーの設定は無視されます。

#### 通知スタイルの追加（任意）

ステータスバーに通知するかダイアログで通知するか通知スタイルを指定します。

通知スタイルを変更したい場合は、ApplicationManifest.xmlのmeta-data要素のcom.coreasp.corepush.notificationStyleのandroid:resource属性に通知スタイルの値を指定します。

##### <i>[通知をステータスバーで表示する場合]</i>
meta-dataに@integer/core\_push\_notification\_style\_statusBarの値を指定します。

```xml
<!-- CORE PUSHの通知スタイル-->
<meta-data android:name="com.coreasp.corepush.notificationStyle" android:resource="@integer/core_push_notification_style_statusBar"/>
```    

##### <i>[通知をダイアログで表示する場合]</i>
meta-dataに@integer/core\_push\_notification\_style\_dialogの値を指定します。
       
```xml
<!-- CORE PUSHの通知スタイル-->
<meta-data android:name="com.coreasp.corepush.notificationStyle" android:resource="@integer/core_push_notification_style_dialog"/>
```

##### <i>[通知をステータスバーとダイアログで同時に表示する場合]</i> 
meta-dataに@integer/core\_push\_notification\_style\_dialog_and_statusBarの値を指定します。

```xml
<!-- CORE PUSHの通知スタイル-->
<meta-data android:name="com.coreasp.corepush.notificationStyle" android:resource="@integer/core_push_notification_style_dialog_and_statusBar"/>
```

meta-dataの読み込みに失敗した場合は、通知スタイルは[通知をステータスバーで表示する場合]がデフォルト設定になります。


##### <i>通知チャンネルの設定</i> 
Android8.0以上では、ユーザーがプッシュ通知を管理しやすいように通知チャンネルが導入されました。

SDKにて通知チャンネルを利用するには、以下の要領でアプリのAndroidManifest.xmlの中で、「Channel ID」、「Channel Name」、「Channel Description」、「show badge」を定義してください。「Channel ID」と「Channel Name」は必須項目ですが、その他の項目は任意です。指定しない場合、「Channel Description」は空文字、「Show Badge」はtrueとなります。

><b>注意：</b>
> 
> もし、アプリのtargetSDKVersionが26以上の場合、 「Channel ID」および「Channel Name」は必須項目となります。これらを宣言しない場合、Android8.0以上では、SDKはCoreASPから送信したプッシュ通知を表示しません。

```xml
        <meta-data android:name="com.coreasp.corepush.notificationChannelId" android:value="@string/YOUR_CHANNEL_ID"/>
        
        <meta-data android:name="com.coreasp.corepush.notificationChannelName" android:value="@string/YOUR_CHANNEL_NAME"/>
        
        <meta-data android:name="com.coreasp.corepush.notificationChannelDescription" android:value="@string/YOUR_CHANNEL_DESCRIPTION"/>
        
        <meta-data android:name="com.coreasp.corepush.notificationShowBadge" android:value="true"/>
```

アプリのtargetSDKVersionが25以下の場合、もしくはデバイスがAndroid8.0より以前のバージョンにて動作している場合、SDKはこの宣言を無視します。

><b>追記:</b>
>
>通知チャンネルはAndroid8.0以降の標準機能として搭載されています。詳しくは以下のサイトをご参照ください：
https://developer.android.com/guide/topics/ui/notifiers/notifications#ManageChannels.


#### 通知の複数表示設定の追加（任意）

ステータスバーで通知を複数件表示するか、最新の一件を表示するか設定することができます。ApplicationManifest.xmlのmeta-data要素のcom.coreasp.corepush.notificationCollapsedのandroid:value属性に真偽値を指定します。

```xml
<!-- CORE PUSHの通知の複数表示設定。 -->
<!-- ステータスバーに最新の１件を表示する場合はtrue、複数件表示する場合はfalseを指定します。デフォルト値はtrueになります。 -->
<meta-data android:name="com.coreasp.corepush.notificationCollapsed" android:value="false"/>
```

meta-dataの読み込みに失敗した場合は、[最新の1件を表示]の表示設定になります。

#### デバッグログ出力設定の追加（任意）
デバッグログの出力制御を設定することができます。ApplicationManifest.xmlのmeta-data要素のcom.coreasp.debugLogEnabledのandroid:value属性に真偽値を指定します。true(デバッグログ有効)が指定された場合は、COREASPタグのDEBUGログレベルでログ出力を行います。

```xml
<!-- CORE ASPのデバッグログの有効化フラグ -->
<meta-data android:name="com.coreasp.debugLogEnabled" android:value="true"/>
```

meta-dataの読み込みに失敗した場合は、false(デバッグログ無効)の設定になります。

## SDKの利用

### SDKの初期化
------

Applicationクラスを継承したサブクラスでSDKの初期化を行います。SDKの初期化を行うには、以下の手順を実行します。

1. Applicationクラスのサブクラスを作成します。ここでは、MyApplicationクラスを作成したと仮定します。（既に使用中のApplicationクラスのサブクラスがある場合は、そのクラスを使用してください）
2. MyApplicationクラスをマニフェストに追加します。AndroidManifest.xmlのapplication要素のandroid:name属性にMyApplicationクラスを記述します。
	
	```xml
	<application android:name=".MyApplication">
	...
	</application>
	```

3. MyApplicationクラスのonCreateメソッド内でCoreAspManager#initializeメソッドを呼び出します。initializeメソッドの引数には、ApplicaitonContext（Application）オブジェクトを渡します。
	
	```java
	/**
	 * Applicationクラスのサブクラス
	 */
	public class MyApplication extends Application {
	
	  @Override
	  public void onCreate() {
	    super.onCreate();
	    
	    // COREASPマネージャーの初期化処理
	    CoreAspManager.initialize(this);
	  }
	}
	```


### デバイスの通知登録解除
------
デバイスが通知を受信できるようにするには、CORE ASPサーバにデバイストークンを送信します。またデバイスが通知を受信できないようにするには、CORE ASPサーバからデバイストークンを削除します。

#### デバイストークンの登録

CORE ASPサーバにデバイストークンを登録するには、CorePushManager#registerToken を呼び出します。

```	java
CorePushManager.getInstance().registerToken();
```
	
本メソッドはアプリの初回起動時やON/OFFスイッチなどで通知をONにする場合に使用してください。
		
#### デバイストークンの削除

CORE ASPサーバからデバイストークンを削除するには、CorePushManager#unregisterToken を呼び出します。

```java
CorePushManager.getInstance().unregisterToken();
```
		
本メソッドはON/OFFスイッチなどで通知をOFFにする場合に使用してください。

### 通知履歴の表示
------

#### 通知履歴の取得
CorePushNotificationHistoryManager#requestNotificationHistory を呼び出すことで通知履歴を最大100件取得できます。

```java
// 通知履歴取得リクエストの実行
CorePushNotificationHistoryManager manager = new CorePushNotificationHistoryManager(this);
manager.requestNotificationHistory(new CorePushNotificationHistoryManager.CompletionHandler() {
  @Override
  public void notificationHistoryManagerSuccess(List<CorePushNotificationHistoryModel> notificationHistoryModelList) {
     
  }

  @Overrid
  public void notificationHistoryManagerFail() {

  }
});
```

取得した通知履歴のオブジェクトの配列は、CorePushNotificationHistoryManager.CompletionHandlerクラスのnotificationHistoryManagerSuccessメソッドの引数に渡されます。

```java
new CorePushNotificationHistoryManager.CompletionHandler() {
  @Override
  public void notificationHistoryManagerSuccess(List<CorePushNotificationHistoryModel> notificationHistoryModelList) {
     
  }

  @Override
  public void notificationHistoryManagerFail() {

  }
}
```
	
  
上記の配列により、個々の通知履歴の CorePushNotificationHistoryModel オブジェクトを取得できます。CorePushNotificationHistoryModelオブジェクトには、履歴ID、通知メッセージ、通知日時、リッチ通知URLが格納されます。

```
// 例) 451
String historyId = historyModel.getHistoryId();
	
// 例) CORE PUSH からのお知らせ!
String message = historyModel.getMessage();

// 例) http://core-asp.com
String* url = historyModel.getUrl();

// 例) 2017-08-10 17:48:30
String* regDate = historyModel.getRegDate();
```


### カテゴリの設定
------
#### １次元カテゴリ設定
デバイストークン登録APIの category_id パラメータの設定を行うことができます。パラメータの設定を行うには、
CorePushManager#setCategoryIds で カテゴリID(文字列型)のリストを指定します。以下はカテゴリIDのリストの作成例になります。<br/>
※例は事前に管理画面で1から4までのカテゴリを設定しておいたものと仮定します。
    
```java
// 1:北海道、2:東北 3:関東、4:近畿
List<String> categoryIds = Arrays.asList("1", "2", "3", "4");
CorePushManager.getInstance().setCategoryIds(categoryIds);
```
    
上記カテゴリの設定後にデバイストークンを送信した場合、設定したcategory_id パラメータの値をCORE ASPサーバにPOSTします。
(category_idパラメータを設定しない場合のデフォルト値は 1 になります。)

#### ２次元カテゴリ設定
デバイストークン登録APIの category_id パラメータの設定を行うことができます。パラメータの設定を行うには、
CorePushManager#setMultiCategoryIds で カテゴリIDのマップを指定します。以下はカテゴリIDのマップの作成例になります。<br/>
※例は事前に管理画面で1から4までのカテゴリを設定しておいたものと仮定します。

```java
// 1:地域、2:性別 3:年代 4:好きなジャンル(複数選択可の場合)
HashMap<String, List<String>> multiCategoryIds = new HashMap<>();
multiCategoryIds.put("1", Arrays.asList("神奈川")); // 地域が「神奈川」の場合
multiCategoryIds.put("2", Arrays.asList("男性"));   // 性別が「男性」の場合
multiCategoryIds.put("3", Arrays.asList("20代"));   // 年代が「20代」の場合
multiCategoryIds.put("4", Arrays.asList("音楽", "読書")); // 好きなジャンルが「音楽」と「読書」の場合
CorePushManager.getInstance().setMultiCategoryIds(multiCategoryIds);
```
    
上記カテゴリの設定後にデバイストークンを送信した場合、設定したcategory_id パラメータの値をCORE ASPサーバにPOSTします。
(1次元カテゴリと2次元カテゴリの両方が設定されている場合、category_id パラメータには２次元カテゴリの設定が優先されます。category_idパラメータを設定しない場合のデフォルト値は 1 になります。)
    
    
### ユーザー間プッシュ通知
------

ユーザー間のプッシュ通知を実現するには、事前にアプリ側でユーザーのデバイストークンのCORE ASPサーバへの登録とユーザー属性の御社サーバへの登録を行う必要があります。全体のイメージ図につきましては、<a href="http://developer.core-asp.com/api_image.php">http://developer.core-asp.com/api_image.php</a> をご参照ください。

#### CORE ASPサーバへのデバイストークンの登録

デバイストークンの登録を行う前に、CorePushManager#setAppUserIdでアプリ内のユーザーIDを指定します。

```java
// アプリ内でのユーザーの識別IDを登録
CorePushManager.getInstance().setAppUserId("userid");

// デバイストークンの登録
CorePushManager.getInstance()
```
  
上記により、api.core-asp.com/android_token_regist.php のトークン登録APIに
対して、app_user_id のパラメータが送信され、アプリ内でのユーザーの識別IDとデバイストークンが
紐づいた形でDBに保存されます。
  
#### 御社サーバへのユーザー属性の登録

CorePushRegisterUserAttributeManager#registerUserAttributes で御社サーバにユーザー属性の登録を行う前に
、CorePushManager#setAppUserIdでアプリ内でのユーザーの識別IDを指定します。

```java
// アプリ内でのユーザーの識別IDを登録
CorePushManager.getInstance().setAppUserId("userid");
```

ユーザー属性を定義したリストを作成します。

```java   
// ユーザー属性の配列を作成。例) 1:いいね時の通知許可、3:コメント時の通知許可、7:フォロー時の通知許可
List<String> attributes = Arrays.asList("1,3,7");
```

ユーザー属性を送信する御社サーバ上の任意のURLを指定します。

```java
// ユーザー属性を送信する御社の任意のURLを指定
String attributeRegisterUrl = "[ユーザ属性を送信する御社の任意のURL]"; 
```

CorePushRegisterUserAttributeManagerオブジェクトのregisterUserAttributesメソッドを呼び出すことで、アプリ内でのユーザーの識別IDとユーザー属性を御社サーバに送信します。メソッドの引数として、作成したユーザー属性を定義した配列とユーザー属性を送信するAPIのURL、送信完了時に呼び出されるCompletionHandlerクラスを指定します。

```java
// アプリ内でのユーザーの識別IDとユーザー属性の送信
userAttributeManager.registerUserAttributes(attributes, attributeRegisterUrl, new CorePushRegisterUserAttributeManager.CompletionHandler() {
  @Override
  public void registerUserAttributeManagerSuccess() {
    Toast.makeText(HomeActivity.this, "ユーザー属性登録成功", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void registerUserAttributeManagerFail() {
    Toast.makeText(HomeActivity.this, "ユーザー属性登録失敗", Toast.LENGTH_SHORT).show();
  }
});
```

特定のユーザーに対してプッシュ通知を行うには、通知送信リクエストAPIに対して、御社サーバから通知の送信依頼
を行います。詳細につきましては、<a href="http://developer.core-asp.com/api_request.php">http://developer.core-asp.com/api_request.php</a> をご参照ください。

### アクセス解析
------

#### 通知からの起動数の把握

通知からのアプリの起動時にアクセス解析用のパラメータをCORE ASPサーバに対して送信することで、管理画面の通知履歴から通知からの起動数を把握することができます（ただし、通知からのアプリ起動数の把握は、本番環境のプッシュ通知のみに制限されます。開発環境のプッシュ通知では、後述の通知IDを取得することができません)。

アクセス解析用のパラメータを CORE ASPサーバに対して送信するには、通知から起動される Intentオブジェクトから CorePushManager#getPushIdで通知IDを取得し、CorePushAppLaunchAnalyticsManager#requestAppLaunchAnalyticsで
通知IDを送信します。requestAppLaunchAnalyticsメソッドの引数には、通知ID、緯度、経度の値を順に指定します。アプリ起動時の緯度・経度を送信しない場合は、0を指定します。

```java
// intentオブジェクトから通知IDを取得する
String pushId = manager.getPushId(intent);

if (pushId != null) {
  CorePushAppLaunchAnalyticsManager appLaunchAnalyticsManager = new CorePushAppLaunchAnalyticsManager(this);
  appLaunchAnalyticsManager.requestAppLaunchAnalytics(pushId, "0", "0", new CorePushAppLaunchAnalyticsManager.CompletionHandler() {
    @Override
    public void appLaunchAnalyticsManagerSuccess() {
      Toast.makeText(HomeActivity.this, "起動数送信成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void appLaunchAnalyticsManagerFail() {
      Toast.makeText(HomeActivity.this, "起動数送信失敗", Toast.LENGTH_SHORT).show();

    }
  });
}
```

### 分散配信機能について
------

※分散配信機能を使用する場合は、SDKのバージョン1.2.0以上を推奨します。

分散配信機能は、配信対象の通知を複数のグループに分割し、分割したグループごとに通知を配信する機能になります。分散配信された通知の通知IDの値は、分割したグループごとに異なる値が割り当てられます。

この通知IDと管理画面のアクション設定に記載のアクションキーの組み合わせをアクセス解析用のパラメータとして CORE ASPサーバに対して送信することで、分散配信された通知の起動数や各アクション数を分析することができます。

アクションキーを含めたアクセス解析用のパラメータを CORE ASPサーバに送信するには、CorePushAppLaunchAnalyticsManager#requestAppLaunchAnalyticsメソッドを使用します。

```java
String pushId = ＜起動時に取得した通知IDの値＞

// アクションキーの101を指定した場合
if (pushId != null) {
  CorePushAppLaunchAnalyticsManager appLaunchAnalyticsManager = new CorePushAppLaunchAnalyticsManager(this);
  appLaunchAnalyticsManager.requestAppLaunchAnalytics(pushId, "101", "0", "0", new CorePushAppLaunchAnalyticsManager.CompletionHandler() {
    @Override
    public void appLaunchAnalyticsManagerSuccess() {
      Toast.makeText(HomeActivity.this, "送信成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void appLaunchAnalyticsManagerFail() {
      Toast.makeText(HomeActivity.this, "送信失敗", Toast.LENGTH_SHORT).show();

    }
  });
}
```
        
### プッシュ通知の送信エラー
------

#### エラー内容の把握

プッシュ通知の送信に失敗した場合、管理画面の送信履歴のエラー数のリンク先からエラー画面を確認できます。
エラー区分としては下記に分類されます。

1. アプリ削除でトークンが無効となった場合や、形式不正なトークンなどによるエラー
2. 上記以外のエラー（通信失敗、その他）


#### 通知メッセージの制限について
メッセージペイロード(JSON形式)の長さはタイトルとメッセージを合わせて1024バイト以内で入力してください。 
(全角:3バイト、半角カナ:3バイト、記号:1バイト、半角英数:1バイト) 
<div>※ 全角文字340文字もしくは半角英数1024文字が目安になります。</div>

## AARモジュールのマージ項目
AARモジュールを使用することで、リソースファイルやアプリのマニフェストにパーミッションやサービスクラスなどがマージされます。ここでは、自動的にマージされる内容について記述します。

### マージされるパーミッション
------

FCMのモジュールのcom.google.firebase:firebase-messagingをインストールすることで、以下のパーミッションがアプリケーションレベルのAndroidManifest.xmlにマージされます。

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />

<permission
    android:name="＜アプリケーションパッケージ名＞.permission.C2D_MESSAGE"
    android:protectionLevel="signature" />

<uses-permission android:name="＜アプリケーションパッケージ名＞.permission.C2D_MESSAGE" />
```

### マージされるクラス
------

FCMのモジュールのcom.google.firebase:firebase-messagingをインストールすることで、以下のクラスがアプリケーションレベルのAndroidManifest.xmlにマージされます。

```xml
<service
    android:name="com.google.firebase.messaging.FirebaseMessagingService"
    android:exported="true" >
    <intent-filter android:priority="-500" >
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>

<receiver
    android:name="com.google.firebase.iid.FirebaseInstanceIdReceiver"
    android:exported="true"
    android:permission="com.google.android.c2dm.permission.SEND" >
    <intent-filter>
        <action android:name="com.google.android.c2dm.intent.RECEIVE" />

        <category android:name="com.example.maru.mypermissionapplication" />
    </intent-filter>
</receiver>

<!--
Internal (not exported) receiver used by the app to start its own exported services
     without risk of being spoofed.
-->
<receiver
    android:name="com.google.firebase.iid.FirebaseInstanceIdInternalReceiver"
    android:exported="false" />
    
<!--
FirebaseInstanceIdService performs security checks at runtime,
     no need for explicit permissions despite exported="true"
-->
<service
    android:name="com.google.firebase.iid.FirebaseInstanceIdService"
    android:exported="true" >
    <intent-filter android:priority="-500" >
        <action android:name="com.google.firebase.INSTANCE_ID_EVENT" />
    </intent-filter>
</service>

<provider
    android:name="com.google.firebase.provider.FirebaseInitProvider"
    android:authorities="com.example.maru.mypermissionapplication.firebaseinitprovider"
    android:exported="false"
    android:initOrder="100" />

```

CORE ASPのAARモジュールをインストールすることで、以下のCORE ASP固有のクラスがアプリケーションレベルのAndroidManifest.xmlにマージされます。

```xml
<!-- CorePushMessagingService -->
<service android:name="com.coreasp.CorePushMessagingService" >
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>

<!-- CorePushInstanceIDService -->
<service android:name="com.coreasp.CorePushInstanceIDService" >
    <intent-filter>
        <action android:name="com.google.firebase.INSTANCE_ID_EVENT" />
    </intent-filter>
</service>

<!-- CorePushRegistrationIntentService -->
<service
    android:name="com.coreasp.CorePushRegistrationIntentService"
    android:exported="false" />
    
<!-- プッシュ通知のダイアログ用のアクティビティ -->
<activity
    android:name="com.coreasp.CorePushDialog"
    android:excludeFromRecents="true"
    android:launchMode="singleInstance"
    android:theme="@android:style/Theme.Dialog" >
</activity>

```

### マージされるリソース
------

CORE ASPのAARモジュールをインストールすることで、SDKの通知スタイルのリソースファイルがアプリケーションレベルのリソースにマージされます。

```xml
<resources>
    <integer name="core_push_notification_style_statusBar">0</integer>
    <integer name="core_push_notification_style_dialog">1</integer>
    <integer name="core_push_notification_style_dialog_and_statusBar">2</integer>
</resources>
```

### マージされるProGuard設定
------

ProGuard設定は、AARモジュールの一部として提供されます。
CORE ASPのAARモジュールをインストールすることで、以下のCORE ASP固有のProGuard設定がアプリモジュールのProGuard設定ファイル(proguard.txt)に追加されます。

```xml
-keep public class com.coreasp.** { public *; }
-dontwarn com.coreasp.**
```
