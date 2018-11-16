package dbook

trait DiaryItem {
  protected var _id: Int = 0
  protected var _title: String = ""
  protected var _timeCreated: Long = 0
  protected var _tags: List[String] = List()

  def id: Int = _id
  def title: String = _title
  def timeCreated : Long = _timeCreated
  def tags: List[String] = _tags

  def id_=(id: Int): Unit = _id = id
  def title_=(title: String): Unit = _title = title
  def timeCreated_=(time: Long): Unit = _title = title

  def hasTag(tag: String) :Boolean = false
}

