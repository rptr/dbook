package dbook

import java.util.Calendar

/*
 * dbook.Diary is a collection of Entries and Chapters
 */

class Diary {
  private val _dateCreated :Int = 0

  private var _title :String = ""
  private var _fileName :String = ""
  private var _entries :List[DiaryItem] = List()
  private var _nextItemId :Int = 0

  private def addItem (): Unit = {
    println("TODO addItem")
  }

  private def addItem (parentItemIndex: Int): Unit = {
    println("TODO addItem")
  }

  private def nextItemId (): Int = {
    _nextItemId += 1
    _nextItemId
  }

  def loadDefault (): Unit = {
    addEntry()
    addEntry()
    addEntry()
    addEntry()
  }

  def addEntry (): Unit = {
    val time: Long = Calendar.getInstance().getTime.getTime
    val e = new Entry(nextItemId(), "hi", time, "im empty inside")
    _entries = e :: _entries
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

  def getEntry (id: Int): Option[DiaryItem] = {
    _entries find (e => e.id == id)
  }

  def saveEntry (entryId: Int, text: String): Unit = {
    var i = getEntry(entryId)

    if (i.nonEmpty && i.get.isEntry) {
      _entries = _entries.updated(_entries.indexWhere(e => e.id == entryId),
        new Entry(i.get.id, i.get.title, i.get.timeCreated, text))
    }
  }

  def getAllItems(): List[DiaryItem] = _entries
}
