package dbook

import java.awt.Point
import java.awt.event.KeyListener

import javax.swing.UIManager

import scala.swing.TabbedPane.Page
import scala.swing.event._
import swing._

import dbook.Config

class EntryListItem(entry: DiaryItem) {
  var _entryId: Int = entry.id

  override def toString: String = "2018: " + "dear diary"
}

/**
  * A simple swing demo.
  */
object Main extends SimpleSwingApplication {
  val fileChooser   : FileChooser = new FileChooser()
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
  val tabBox        : TabbedPane = new TabbedPane()

  val statusPanel   : FlowPanel = new FlowPanel(FlowPanel.Alignment.Left)()
  val tagsMenu      : MenuBar = new MenuBar()

  // ENTRY LIST
  val entryList     : ListView[EntryListItem] = new ListView[EntryListItem]() {
    listenToKeys(this)
  }

  val entryListHolder : BoxPanel = new BoxPanel(Orientation.Vertical) {
      listenTo(entryList.selection)

      reactions += {
        case SelectionChanged(`entryList`) => {
          val index: Int = entryList.selection.indices.head
          println("TODO click entry " + entryList.listData(index))
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
  def addOnClick(item: Component, cb: () => Any) = {
    item.reactions += {
      case e: ButtonClicked => cb()
    }
  }

  def listenToKeys (c: Component) :Unit = {
    listenTo(c.keys)

    c.reactions += {
      case e: KeyPressed => keyDown(e)
    }
  }

  def top = new MainFrame {
    // setup window
    this.centerOnScreen()
    this.location_=(new Point(this.location.x, 0))

    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")

    title = "lomkal 0.0.1"

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
          addOnClick(this, () => println("TODO exit"))
        }
      }

      contents += new Menu("Entry") {
        contents += new MenuItem("New") {
          addOnClick(this, () => println("TODO new entry"))
        }

        contents += new MenuItem("Delete") {
          addOnClick(this, () => println("TODO delete entry"))
        }
      }

      contents += new Menu("Folder") {
        contents += new MenuItem("New") {
          addOnClick(this, () => println("TODO new folder"))
        }
        contents += new MenuItem("Delete") {
          addOnClick(this, () => println("TODO delte folder"))
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

    editorHolder.contents += tabBox
    editorHolder.contents += statusPanel

    contents = new SplitPane(Orientation.Vertical, editorHolder,
                             entryListHolder)
    {
      dividerLocation = 420
    }

    setup()
  }

  def keyDown (e: KeyPressed) :Unit = {
    e.key match {
      case Config.keyCloseTab => closeCurrentTab()
    }
  }

  def setup() :Unit = {
    setupTags()
    updateEntryList()

    // TESTING
    addTab(new Entry())
    addTab(new Entry())
    addTab(new Entry())
  }

  // TABS
  def addTab(entry: Entry): Unit = {
    val textArea : EditorPane = new EditorPane() {
      listenTo(caret)

      reactions += {
        // TODO change to EditDone
        case e: CaretUpdate => {
          println("TODO text changed")
        }
      }

      listenToKeys(this)
    }

    val editor = new ScrollPane (textArea)

    tabBox.pages += new TabbedPane.Page("tab", editor)
  }

  def closeTab(index: Int): Unit = {
    tabBox.pages.remove(index)
  }

  def closeCurrentTab(): Unit = {
    closeTab(tabBox.pages.indexOf(tabBox.selection))
  }

  // DIARY ENTRIES
  def updateEntryList(): Unit = {
    val allEntries: Seq[EntryListItem] = Seq(new EntryListItem(new Entry()))
    entryList.listData_=(allEntries)
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