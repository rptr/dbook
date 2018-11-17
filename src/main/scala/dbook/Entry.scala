/*
 * An Entry is one "page" of the diary
 */
package dbook

case class Entry() extends DiaryItem {
  private var _timeEdited: Int = 0
  private var _body: String = ""

  def this (id: Int, title: String, time: Long) {
    this()

    _id = id
    _title = title
    _timeCreated = time
  }

  def this (id: Int, title: String, time: Long, body: String) {
    this()

    _id = id
    _title = title
    _timeCreated = time
    _body = body
  }

  def isEntry: Boolean = true
  def isChapter: Boolean = false

  def timeEdited: Int = _timeEdited
  def body: String = _body
  override def title: String = {
    var title = "[no title]"

    if (_body.nonEmpty) {
      val parts = _body.split("\n")

      if (parts.nonEmpty && !parts(0).isEmpty) title = parts(0)
    }

    title
  }

  def body_=(s: String): Unit = _body = s

  def wordCount: Int = 0
}
