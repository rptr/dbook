package dbook

import java.util.Calendar

import scala.collection.mutable.ListBuffer

/*
 * dbook.Diary is a collection of Entries and Chapters
 */

class Diary {
  private val _dateCreated :Int = 0

  private var _title :String = ""
  private var _fileName :String = ""
  private var _entries :ListBuffer[DiaryItem] = ListBuffer()
  private var _nextItemId :Int = 0

  /*
   * DiaryItem
   */
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

  /*
   * Entry
   */
  def addEntry (entry: Entry): Unit = {
    if (entry.id < _nextItemId) {
      entry.id = _nextItemId
    }

    _entries.append(entry)
    _nextItemId = entry.id + 1
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

  def getEntry (id: Int): Option[Entry] = {
    val item = _entries find (e => e.id == id)

    item match {
      case Some(e:Entry) => Some(e)
      case _ => None
    }
  }


  def saveEntry (entryId: Int, text: String): Unit = {
    var i = getEntry(entryId)

    if (i.nonEmpty) {
      _entries.update(_entries.indexWhere(e => e.id == entryId),
        new Entry(i.get.id, "", i.get.timeCreated, text))
    }
  }

  def tagEntry (entryId: Int, tag: String): Unit = {
    _entries(_entries.indexWhere(e => e.id == entryId)).addTag(tag)
  }

  /*
   * Chapter
   */

  def addChapter (chapter: Chapter): Unit = {
    println("TODO append new chapter")
  }

  def addChapter (parentChapterIndex: Int): Unit = {
    println("TODO add new chapter")
  }

  def saveAll (): Unit = {

  }

  def getAllItems: ListBuffer[DiaryItem] = _entries
}
