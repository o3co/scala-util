


## IDProvider Service

IDProvider Serviceは、
  
  - 一意性のみを管理するIDProviderService
  - ドメインに紐づく一意のIDを管理するDomainIDProviderService

を提供します。
主に、グローバルIDや、特定ドメイン内固有のIDを生成します。

````
val provider = DomainIDProviderServiceAdapater[UUID]("en")

// IDの自動生成
provider.generateAsync("content") // Future[UUID]
// IDの存在有無の確認
provider.containsAsync(id, Some("content")) // Future(true)
// 指定IDを予約
provider.reserveAsync(id, Some("image")) // Future(throw DuplicateException(id))
// IDを解放
provider.releaseAsync(id, None) 
````

````
val provider = DomainIDProviderServiceAdapter[UUID]()
````

## Text Service  

Text Serviceは、
 
  - Textデータに一意のキーに紐付けたテキストデータを保存する TextStore 
  - グローバルIDにTextデータとの関係性を紐付けるTextAppendixService
  - 全文検索 FullTextSearchService

を提供します。

### TextStoreService

DomainTextStore
````
val textStore = TextStore[UUID]()
textStore.putAsync(id, "description", textData)

textStore.containsAsync(id) // Future[Boolean]
textStore.deleteAsync(id)   // Future[Unit]
````

### Text Appendix Service

`Text Appendix Service`は、柔軟なテキストデータの挿入に役立ちます。
例えば、"説明文"や"備考"は、多くの場合、追加取得（データの詳細取得）の際のみ必要になるデータです。
このような場合において、他の汎用的なコンテンツと一つのフィールドとして扱っていては、システム全体の遅延が想定されます。



````
val textAppendixService = TextAppendixServiceAdapter()

textAppendixService.putAsync(contentId, "content.description", textId)

// contentIDに紐付いた"content.description"のTextを取得
textAppendixService.getAsync(contentId, Condition.equals("content.description"))  // Future[Option[TextID]]

// contentIDと"content."前方一致のTextを検索する
textAppendixService.searchAsync(TextAppendixCondition(contentId, Condition.prefix("content.")), None, 0, 0)  // Future[Option[TextID]]
````

````
trait ContentManager {

  def list(...) = {
    // contentのみを返す
    contents
  }

  def detail(..) = {
    // content.noteを追加したContentDetailを返す
    textAppendixService.getAsync(contentID, "content.note").map { note => 
      ContentDetail(content, note)    
    }
  }
}

````

## Tag Service

TagServiceは、

  - IDに紐付いたTagName(英数字)を保存するTagStoreService
  - TagNameからTagLabelに変換するTagDictionaryService

を提供します。
エンティティによる束縛ではなく、IDによって束縛されるTagServiceモデルは、一つの共通のTagStoreで管理することが可能です。


````
trait ContentManager {
  def detail(..) = {
    tagStoreService.getAsync(contentID).map { tags =>
      ContentDetail(content, tags)
    }
  }
}

````
