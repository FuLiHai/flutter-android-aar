// Copyright 2016 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HelloServices extends StatefulWidget {
  @override
  _HelloServicesState createState() => new _HelloServicesState();
}

class _HelloServicesState extends State<HelloServices> {
  String _result = '';
  String _filename = '';
  bool _isExisted = false;

  @override
  Widget build(BuildContext context) {
    if (_isExisted) {
      return new Material(
          child: new Center(
              child: new Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: <Widget>[
                    new Text('Barcode Reader'),
                    new Input(
                      labelText: 'Please input the image path',
                      value: new InputValue(text: _filename),
                      onChanged: onTextChanged,
                      autofocus: true,
                    ),
                    new Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: <Widget>[
                          new RaisedButton(
                              child: new Text('Read'),
                              onPressed: _getBarcode
                          ),
                          new RaisedButton(
                              child: new Text('Reset'),
                              onPressed: _resetResult
                          ),
                        ]
                    ),
                    new Image.file(new File(_filename)),
                    new Text('$_result'),
                  ]
              )
          )
      );
    }
    else {
      return new Material(
          child: new Center(
              child: new Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: <Widget>[
                    new Text('Barcode Reader'),
                    new Input(
                      labelText: 'Please input the image path',
                      onChanged: onTextChanged,
                      autofocus: true,
                    ),
                    new Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: <Widget>[
                          new RaisedButton(
                              child: new Text('Read'),
                              onPressed: _getBarcode
                          ),
                          new RaisedButton(
                              child: new Text('Reset'),
                              onPressed: _resetResult
                          ),
                        ]
                    ),
                    new Text('$_result'),
                  ]
              )
          )
      );
    }
  }

  Future<Null> onTextChanged(InputValue value) async {
    _filename = value.text.toString();
  }

  Future<Null> _readBarcode() async {
    final Map<String, String> message = <String, String>{'filename':_filename};
    final Map<String, dynamic> reply = await HostMessages.sendJSON('getBarcode', message);
    // If the widget was removed from the tree while the message was in flight,
    // we want to discard the reply rather than calling setState to update our
    // non-existent appearance.
    if (!mounted)
    return;
    setState(() {
    _result = reply['result'].toString();
    });
  }

  void _getBarcode() {
    File file = new File(_filename);
    bool isExisted = file.existsSync();
    _isExisted = isExisted;
    if (isExisted) {
      _readBarcode();
    }
    else {
      setState(() {
        _result = 'Invalid path';
        _filename = '';
        _isExisted = false;
      });
    }
  }

  Future<Null> _resetResult() async {
    if (!mounted)
      return;
    setState(() {
      _result = '';
      _filename = '';
      _isExisted = false;
    });
  }
}

void main() {
  runApp(new HelloServices());
}
