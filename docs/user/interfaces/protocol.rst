.. highlight:: sh

========
Protocol
========

.. rubric:: Table of contents

.. contents::
   :local:


Introduction
============

SQL plugin provides multiple protocols for different purposes.

CSV Format
==========

And you can also use CSV format to download result set in csv format::

	POST /_opendistro/_sql?format=csv
	{
	  "query": "SELECT firstname, lastname, age, city FROM accounts"
	}

	firstname,lastname,age,cityDale,Adams,33,OrickHattie,Bond,36,DanteNanette,Bates,28,NogalAmber,Duke,32,Brogan

DSL Format
==========

By default the plugin returns original response from Elasticsearch engine in JSON::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT firstname, lastname, age, city FROM accounts LIMIT 2"
	}

	{
	  "schema" : [
	    {
	      "name" : "firstname",
	      "type" : "text"
	    },
	    {
	      "name" : "lastname",
	      "type" : "text"
	    },
	    {
	      "name" : "age",
	      "type" : "long"
	    },
	    {
	      "name" : "city",
	      "type" : "text"
	    }
	  ],
	  "total" : 4,
	  "datarows" : [
	    [
	      "Dale",
	      "Adams",
	      33,
	      "Orick"
	    ],
	    [
	      "Hattie",
	      "Bond",
	      36,
	      "Dante"
	    ]
	  ],
	  "size" : 2,
	  "status" : 200
	}

RAW Format
==========

Additionally you can also use RAW format to pipe the result with other command line tool::

	POST /_opendistro/_sql?format=raw
	{
	  "query": "SELECT firstname, lastname, age, city FROM accounts"
	}

	Dale|Adams|33|OrickHattie|Bond|36|DanteNanette|Bates|28|NogalAmber|Duke|32|Brogan

JDBC Format
===========

JDBC format is provided for JDBC driver or client side that needs schema and data formatted in table::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT firstname, lastname, age, city FROM accounts LIMIT 2"
	}

	{
	  "schema" : [
	    {
	      "name" : "firstname",
	      "type" : "text"
	    },
	    {
	      "name" : "lastname",
	      "type" : "text"
	    },
	    {
	      "name" : "age",
	      "type" : "long"
	    },
	    {
	      "name" : "city",
	      "type" : "text"
	    }
	  ],
	  "total" : 4,
	  "datarows" : [
	    [
	      "Dale",
	      "Adams",
	      33,
	      "Orick"
	    ],
	    [
	      "Hattie",
	      "Bond",
	      36,
	      "Dante"
	    ]
	  ],
	  "size" : 2,
	  "status" : 200
	}

