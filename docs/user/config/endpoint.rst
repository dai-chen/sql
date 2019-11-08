.. highlight:: sh

========
Endpoint
========

.. rubric:: Table of contents

.. contents::
   :local:


Introduction
============

To use SQL plugin, you can use a request parameter or request body by HTTP GET and POST. POST request is recommended because it is more flexible and allows for more parameters passed to plugin.

POST Request
============

You can also send HTTP POST request with your query in request body::

	>> curl -H 'Content-Type: application/json' -X POST http://localhost:9200//_opendistro/_sql ?format=jdbc -d '{
	  "query": "SELECT * FROM accounts"
	}'

GET Request
===========

You can send HTTP GET request with your query embedded in URL::

	>> curl -H 'Content-Type: application/json' -X GET http://localhost:9200//_opendistro/_sql ?format=jdbc&sql=SELECT * FROM accounts

