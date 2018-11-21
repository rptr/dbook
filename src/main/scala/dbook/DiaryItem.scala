package dbook

trait DiaryItem {
  protected var _id: Int = 0
  protected var _title: String = ""
  protected var _timeCreated: Long = 0
  protected var _tags: List[String] = List()

  // NOTE i saw this in some example code, but why do this instead of just
  //      having one class with isChapter/isEntry
  def isChapter: Boolean
  def isEntry: Boolean

  def id: Int = _id
  def title: String = _title
  def timeCreated : Long = _timeCreated
  def tags: List[String] = _tags

  def id_=(id: Int): Unit = _id = id
  def title_=(title: String): Unit = _title = title
  def timeCreated_=(time: Long): Unit = _title = title

  def hasTag (tag: String) :Boolean = _tags.contains(tag)
  def addTag (tag: String) :Unit = _tags = tag :: _tags
}

