package dbook

import java.awt
import java.awt.Point
import java.awt.event.KeyListener
import java.text.SimpleDateFormat
import java.util.Calendar

import javax.swing.{SwingUtilities, UIManager}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.swing._
import scala.swing.event._

import com.github.rjeschke.txtmark

class EntryListItem(entry: DiaryItem) {
  private var _entryId: Int = entry.id

  def entryId: Int = _entryId

  override def toString: String =
    new SimpleDateFormat("yyyy-MM-dd: ").
      format(entry.timeCreated) + entry.title
}

object EditMode extends Enumeration {
  type EditMode = Value
  val Text, Markup = Value
}
import EditMode._

/**
  * A simple swing demo.
  */
object Main extends SimpleSwingApplication {
  val diary :Diary = new Diary()
  var io    :dbook.IO = new PlaintextIO()

  // UI
  val fileChooser   :FileChooser = new FileChooser()
  val preferenceDialog  :Dialog = new Dialog() {

    val panel = new FlowPanel(FlowPanel.Alignment.Left)() {
      preferredSize = new Dimension(300, 300)
      contents += new Label("TODO")
    }

    contents = panel
    title = "Preferences"
  }

  val editorHolder  : BoxPanel = new BoxPanel(Orientation.Vertical)
//  val tabPanel      : FlowPanel = new FlowPanel()

  // TEXT EDITOR
  var tabIndexToEntryId : ListBuffer[Int] = ListBuffer[Int]()
  var tabEditMode       : ArrayBuffer[EditMode] = ArrayBuffer[EditMode]()
  var entryIdToTabIndex : mutable.HashMap[Int, Int] =
    new mutable.HashMap[Int, Int]()
  val tabBox        : TabbedPane = new TabbedPane()

  val statusPanel   : FlowPanel = new FlowPanel(FlowPanel.Alignment.Left)()
  val tagsMenu      : MenuBar = new MenuBar()
  val dateLabel    : Label = new Label("")

  // ENTRY LIST
  val entryRightClickDialog   : Dialog = new Dialog() {
    contents = new FlowPanel(FlowPanel.Alignment.Left)() {
      contents += new Button("Open") {
        addOnClick(this, () => println("TODO edit entry"))
      }

      contents += new Button("Delete") {
        addOnClick(this, () => println("TODO edit entry"))
      }
    }
  }

  val entryList     : ListView[EntryListItem] = new ListView[EntryListItem]()

  val entryListHolder : BoxPanel = new BoxPanel(Orientation.Vertical) {
      listenTo(entryList.selection)
      listenTo(entryList.mouse.clicks)

      reactions += {
        case e @ MouseClicked(_, p, _, num, _) =>
          val index = entryList.peer.locationToIndex(p)

          if (index >= 0) {
            entryList.peer.setSelectedIndex(index)

            val entry: EntryListItem = entryList.listData(index)
            val rmb = SwingUtilities.isRightMouseButton(e.peer)

//            if (rmb)
//              rightClickEntry(entry, p)
            if (num % 2 == 0)
              doubleClickEntry(entry)
            else
              clickEntry(entry)
          }
      }

      val listHolder = new ScrollPane(entryList) {
        preferredSize_=(new Dimension(200, 600))
      }

      val listMenu = new MenuBar() {
        preferredSize = new Dimension(1, 30)

        contents += new Menu("Sort by") {
          contents += new MenuItem("Date") {
            addOnClick(this, () => println("TODO sort by date"))
          }

          contents += new MenuItem("Tag") {
            addOnClick(this, () => println("TODO sort by tag"))
          }
        }

        contents += new Menu("Filter by") {
          contents += new MenuItem("Date") {
            addOnClick(this, () => println("TODO filter by date"))
          }

          contents += new MenuItem("Tag") {
            addOnClick(this, () => println("TODO filter by tag"))
          }
        }
      }

      contents += listMenu
      contents += listHolder
  }

  // UI SETUP METHODS
  def addOnClick(item: Component, cb: () => Any) :Unit = {
    item.reactions += {
      case e: ButtonClicked => cb()
    }
  }

  def listenToKeys (c: Component) :Unit = {
    c.focusable = true
//    c.requestFocus()
    c.listenTo(c.keys)

    c.reactions += {
      case e: KeyPressed => keyDown(e)
    }
  }

  def top: MainFrame = new MainFrame {
    // setup window
    this.centerOnScreen()
    this.location_=(new Point(this.location.x, 0))

    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")

    title = "dbook 0.0.1"

    val dim = new Dimension(600, 600)
    preferredSize = dim
    maximumSize = dim

    // MAIN MENU
    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem("Save") {
          addOnClick(this, () => {
            val d = new Dialog()
            fileChooser.showSaveDialog(d)
          })
        }

        contents += new MenuItem("Load") {
          addOnClick(this, () => {
            val d = new Dialog()
            fileChooser.showOpenDialog(d)
          })
        }

        contents += new MenuItem("Preferences") {
          addOnClick(this, () => {
            preferenceDialog.open()
            preferenceDialog.centerOnScreen()
          })
        }

        contents += new MenuItem("Exit") {
          addOnClick(this, () => {
            diary.saveAll()
            quit()})
        }
      }

      contents += new Menu("Entry") {
        contents += new MenuItem("New") {
          addOnClick(this, () => createNewEntry())
        }

        contents += new MenuItem("Delete") {
          addOnClick(this, () => deleteCurrentEntry())
        }
      }

      contents += new Menu("Folder") {
        contents += new MenuItem("New") {
          addOnClick(this, () => createNewChapter())
        }
        contents += new MenuItem("Delete") {
          addOnClick(this, () => println("TODO delete folder"))
        }
      }

      contents += new Menu("Help") {
        contents += new MenuItem("Documentation") {
          addOnClick(this, () => println("TODO open docs"))
        }
        contents += new MenuItem("About") {
          addOnClick(this, () => println("TODO open about"))
        }
      }
    }

    // STATUS BAR
    statusPanel.preferredSize = new Dimension(1, 40)
    statusPanel.maximumSize = new Dimension(600, 40)
    statusPanel.contents += new Label("5000 words")
    statusPanel.contents += tagsMenu
    statusPanel.contents += new Label("Add tag:")
    statusPanel.contents += new TextField() {
      preferredSize = new Dimension(80, 20)
      listenTo(keys)

      reactions += {
        case e: KeyPressed =>
          if (e.key == Key.Enter) {
            tagCurrentEntry(text)
          }
      }
    }

    editorHolder.contents += tabBox
    editorHolder.contents += statusPanel

    contents = new SplitPane(Orientation.Vertical, editorHolder,
                             entryListHolder) {
      dividerLocation = 420
    }

    setup()
  }

  def keyDown (e: KeyPressed) :Unit = {
    e.key match {
      case Config.keyCloseTab => closeCurrentTab()
      case Key.E => if (e.modifiers == Key.Modifier.Control)
        toggleTabEditMode(currentTabIndex)
      case _ => () => {}
    }
  }

  def setup() :Unit = {
    io.setFile("my_diary")
    io.load(diary)
    io.save(diary)

    setupTags()
    updateEntryList()

    // TESTING
  }

  // TABS
  def openEntryInTab (entry: Entry): Unit = {
    if (!isEntryOpened(entry)) {
      addTab(entry)
    } else {
      val index = entryIdToTabIndex(entry.id)
      tabBox.peer.setSelectedIndex(index)
    }
  }

  def addTab (entry: Entry): Unit = {
    // open text area + tab for this entry
    val textArea : EditorPane = new EditorPane() {
      listenTo(this.keys)

      peer.setContentType("text/plain")

      text = entry.body

      listenToKeys(this)
      this.peer.setFocusTraversalKeysEnabled(false)
    }

    val editor = new ScrollPane (textArea)

    tabBox.pages += new TabbedPane.Page("tab", editor)

    val index = tabBox.pages.length - 1
    tabIndexToEntryId.insert(index, entry.id)
    entryIdToTabIndex.update(entry.id, index)

    tabBox.peer.setSelectedIndex(index)
    setTabEditMode(index, EditMode.Text)

    textArea.reactions += {
      case e: KeyPressed =>
        val mode = tabEditMode(index)

        // save only the markup code
        if (mode == EditMode.Markup) {
          diary.saveEntry(entry.id, textArea.text)
          io.save(diary)
          updateEntryListItem(entry.id)
        }
    }
  }

  /*
   * Check if entry is opened in a tab already.
   */
  def isEntryOpened (entry: Entry): Boolean = {
    entryIdToTabIndex.get(entry.id).nonEmpty
  }

  def toggleTabEditMode (index: Int): Unit = {
    val mode = tabEditMode(index)

    if (mode == EditMode.Markup) setTabEditMode(index, EditMode.Text)
    if (mode == EditMode.Text) setTabEditMode(index, EditMode.Markup)
  }

  def setTabEditMode (index: Int, mode: EditMode): Unit = {
    tabEditMode.insert(index, mode)

    val editor = tabBox.pages(index).content.asInstanceOf[ScrollPane].
      contents.head.asInstanceOf[EditorPane]
    val entry = getEntryFromTabIndex(index)

    if (entry.nonEmpty) {
      var text = entry.get.body

      if (mode == EditMode.Markup) {
        editor.peer.setContentType("text/plain")
        editor.peer.setEditable(true)
        editor.text = text
      } else {
        editor.peer.setContentType("text/html")
        editor.peer.setEditable(false)
        editor.text = txtmark.Processor.process(text)
      }
    }
  }

  def closeTab(tabIndex: Int): Unit = {
    if (tabBox.pages.length > tabIndex && tabIndex >= 0) {
      val entryId = tabIndexToEntryId(tabIndex)

      entryIdToTabIndex.remove(entryId)
      tabIndexToEntryId.remove(tabIndex)
      tabBox.pages.remove(tabIndex)

      println("remove tab "+tabIndex)
    }
  }

  def closeCurrentTab(): Unit = {
    closeTab(currentTabIndex)
  }

  def currentTabIndex: Int = {
    tabBox.pages.indexOf(tabBox.selection.page)
  }

  def switchTab (dir: Int): Unit = {
    tabBox.peer.setSelectedIndex(tabBox.peer.getSelectedIndex %
      tabBox.peer.getTabCount)
  }

  def getEntryFromTabIndex (index: Int): Option[Entry] = {
    val id = tabIndexToEntryId(index)
    diary.getEntry(id)
  }

  // DIARY ENTRIES
  def updateEntryList(): Unit = {
    val allItems = diary.getAllItems
    val allEntries: Seq[EntryListItem] = for (e <- allItems) yield new EntryListItem(e)
    entryList.listData_=(allEntries)
  }

  def updateEntryListItem(entryId: Int): Unit = {
    updateEntryList()
  }

  def openEntry (item: EntryListItem): Unit = {
    var id = item.entryId
    var entry = diary.getEntry(id)

    entry match {
      case Some(e:Entry) => openEntryInTab(e)
      case _ => println("error: no such entry")
    }
  }

  def doubleClickEntry(item: EntryListItem): Unit = {
  }

  def clickEntry(item: EntryListItem): Unit = {
    openEntry(item)
  }

  def rightClickEntry (item: EntryListItem, position: Point): Unit = {
    entryRightClickDialog.open()
    entryRightClickDialog.location = position
  }

  def createNewEntry (): Unit = {
    diary.newEntry()
    updateEntryList()
  }

  def deleteCurrentEntry(): Unit = {
    diary.deleteEntry()
  }

  def tagCurrentEntry (tag: String): Unit = {
    val entryId = tabIndexToEntryId(currentTabIndex)

    diary.tagEntry(entryId, tag)
  }

  def createNewChapter(): Unit = {
    println("todo new chap")
  }

  // TAGS
  def setupTags(): Unit = {
    tagsMenu.contents += new Menu("Tags") {
      contents += new MenuItem("Dream")
      {
        addOnClick(this, () => println("TODO set tag"))
      }
    }
  }


}