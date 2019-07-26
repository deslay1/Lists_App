"Listy", an app with a tabbed activity in order to create 3 different tabs with each their own ListView. The idea is that the user can register items in 3 different lists for the sake of convenience.

Items are stored/removed/updated in an Android SQLite database where each list corresponds to a specific column in the database. As such, each row contains only "one" item.

Includes basic functionality such as:
- Navigation bar with 3 tabs
- Adding items through an EditText field and button onClick()
- Floating context menu appears with an edit and delete option when long pressing on an item.
  - Choosing delete will trigger an alert dialog with an EditText field and two buttons to cancel/apply.
- Bullet points: items loaded from database will always be bulleted
