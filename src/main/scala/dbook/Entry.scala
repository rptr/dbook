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

  def timeEdited: Int = _timeEdited
  def body: String = _body

  def wordCount: Int = 0
}
