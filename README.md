# AstroLumia

This Android project uses [Chaquopy](https://chaquo.com/) to integrate Python code.

## Python Interpreter

Chaquopy requires access to a Python 3 interpreter when building the app. The
`app/build.gradle` file now defaults to using `python3` which should resolve
unresolved reference warnings from IDEs.

If your environment uses a different command for Python 3, edit the `buildPython`
line in `app/build.gradle` accordingly, e.g.

```gradle
python {
    buildPython "path/to/your/python"
}
```
