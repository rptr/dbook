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

  def addItem (item: DiaryItem): Unit = {
    item match {
      case e: Entry => addEntry(e)
      case c: Chapter => addChapter(c)
      case _ =>
    }
  }

  def addItem (parentItemIndex: Int): Unit = {
    println("TODO addItem")
  }

  private def nextItemId (): Int = {
    _nextItemId += 1
    _nextItemId
  }

  def addEntry (entry: Entry): Unit = {
    _entries = entry :: _entries
  }

  def newEntry (): Unit = {
    val time: Long = Calendar.getInstance().getTime.getTime
    addEntry (new Entry(nextItemId(), "", time, ""))
  }

  def deleteEntry (): Unit = {

  }

  def addEntry (parentChapterIndex: Int): Unit = {
    println("TODO add new entry")
  }

  def addChapter (chapter: Chapter): Unit = {
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

  def saveAll (): Unit = {

  }

  def getAllItems: List[DiaryItem] = _entries
}
