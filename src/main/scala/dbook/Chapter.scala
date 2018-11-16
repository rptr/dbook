/*
 * A Chapter is a collection of Entries and other Chapters
 */
package dbook

class Chapter extends DiaryItem {
  private var _children :List[DiaryItem] = List()

  def children: List[DiaryItem] = _children

}
