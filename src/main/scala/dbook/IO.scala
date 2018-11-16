package dbook


trait IO {
  var _file: String = ""

  def setFile (file: String): Unit = {
    _file = file
  }

  def save (diary: Diary): Boolean
  def load (diary: Diary): Boolean
}
