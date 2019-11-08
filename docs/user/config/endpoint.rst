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

GET Request
===========

You can send HTTP GET request with your query embedded in URL::

	>> curl -H 'Content-Type: application/json' -X GET http://localhost:9200//_opendistro/_sql ?format=jdbc&sql=SELECT * FROM accounts

+-----------------------+------------------+---------------+-------------+----------------+-----------------+--------------+--------------------------+----------------------+-----------------+------------+
|  account_number (long)|  firstname (text)|  gender (text)|  city (text)|  balance (long)|  employer (text)|  state (text)|              email (text)|        address (text)|  lastname (text)|  age (long)|
+=======================+==================+===============+=============+================+=================+==============+==========================+======================+=================+============+
|                     13|           Nanette|              F|        Nogal|           32838|          Quility|            VA|  nanettebates@quility.com|    789 Madison Street|            Bates|          28|
+-----------------------+------------------+---------------+-------------+----------------+-----------------+--------------+--------------------------+----------------------+-----------------+------------+
|                      6|            Hattie|              M|        Dante|            5686|           Netagy|            TN|     hattiebond@netagy.com|    671 Bristol Street|             Bond|          36|
+-----------------------+------------------+---------------+-------------+----------------+-----------------+--------------+--------------------------+----------------------+-----------------+------------+
|                      1|             Amber|              M|       Brogan|           39225|           Pyrami|            IL|      amberduke@pyrami.com|       880 Holmes Lane|             Duke|          32|
+-----------------------+------------------+---------------+-------------+----------------+-----------------+--------------+--------------------------+----------------------+-----------------+------------+
|                     18|              Dale|              M|        Orick|            4180|            Boink|            MD|       daleadams@boink.com|  467 Hutchinson Court|            Adams|          33|
+-----------------------+------------------+---------------+-------------+----------------+-----------------+--------------+--------------------------+----------------------+-----------------+------------+


POST Request
============

You can also send HTTP POST request with your query in request body and explain it to Elasticsearch domain specific language (DSL) in JSON::

	>> curl -H 'Content-Type: application/json' -X POST http://localhost:9200//_opendistro/_sql ?format=jdbc -d '{
	  "query": "SELECT * FROM accounts"
	}'

	{
	  "from" : 0,
	  "size" : 200
	}

