trait DiaryItem {
  private var _id: Int = 0
  private var _title: String = ""
  private var _timeCreated: Int = 0
  private var _tags: List[String] = List()

  def id: Int = _id
  def title: String = _title
  def timeCreated : Int = _timeCreated
  def tags: List[String] = _tags

  def hasTag(tag: String) :Boolean = false
}
