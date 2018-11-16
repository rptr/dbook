/*
 * Diary is a collection of Entries and Chapters
 */

class Diary {
  private val _dateCreated :Int = 0

  private var _title :String = ""
  private var _fileName :String = ""
  private var _entries :List[DiaryItem] = List()

  private def addItem (): Unit = {
    println("TODO addItem")
  }

  private def addItem (parentItemIndex: Int): Unit = {
    println("TODO addItem")
  }

  def addEntry (): Unit = {
    println("TODO append new entry")
  }

  def addEntry (parentChapterIndex: Int): Unit = {
    println("TODO add new entry")
  }

  def addChapter (): Unit = {
    println("TODO append new chapter")
  }

  def addChapter (parentChapterIndex: Int): Unit = {
    println("TODO add new chapter")
  }
}
