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

You can also send HTTP POST request with your query in request body and explain it to Elasticsearch domain specific language (DSL) in JSON::

	>> curl -H 'Content-Type: application/json' -X POST localhost:9200/_opendistro/_sql?format=jdbc -d '{
	  "query": "SELECT * FROM accounts"
	}'

Explain Query
=============

You can check out the translation of your query by explain endpoint. The explain output is Elasticsearch domain specific language (DSL) in JSON format::

	>> curl -H 'Content-Type: application/json' -X POST localhost:9200/_opendistro/_sql/_explain -d '{
	  "query": "SELECT firstname, lastname FROM accounts WHERE balance > 10000"
	}'
	{
	  "from" : 0,
	  "size" : 200,
	  "query" : {
	    "bool" : {
	      "filter" : [
	        {
	          "bool" : {
	            "must" : [
	              {
	                "range" : {
	                  "balance" : {
	                    "from" : 10000,
	                    "to" : null,
	                    "include_lower" : false,
	                    "include_upper" : true,
	                    "boost" : 1.0
	                  }
	                }
	              }
	            ],
	            "adjust_pure_negative" : true,
	            "boost" : 1.0
	          }
	        }
	      ],
	      "adjust_pure_negative" : true,
	      "boost" : 1.0
	    }
	  },
	  "_source" : {
	    "includes" : [
	      "firstname",
	      "lastname"
	    ],
	    "excludes" : [ ]
	  }
	}

GET Request
===========

You can send HTTP GET request with your query embedded in URL::

	>> curl -H 'Content-Type: application/json' -X GET localhost:9200/_opendistro/_sql?format=jdbc&sql=SELECT * FROM accounts

