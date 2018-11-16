/*
 * An Entry is one "page" of the diary
 */
package dbook

case class Entry() extends DiaryItem {
  private var _timeEdited: Int = 0
  private var _body: String = ""

  def timeEdited: Int = _timeEdited
  def body: String = body

  def wordCount: Int = 0
}
