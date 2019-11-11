.. highlight:: sh

=============
SQL Functions
=============

.. rubric:: Table of contents

.. contents::
   :local:


Introduction
============

In SQL standard, many useful functions are provided and built in database implementations to help user ...

ABS
===

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT ABS(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "ABS_1" : {
	      "script" : {
	        "source" : "def abs_1 = Math.abs(10);return abs_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+----------------+
	|  ABS_1 (double)|
	+================+
	|              10|
	+----------------+
	

ASCII
=====

Syntax: [(STRING T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT ASCII('abc') FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "ASCII_1" : {
	      "script" : {
	        "source" : "def ascii_1 = (int) 'abc'.charAt(0);return ascii_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+-------------------+
	|  ASCII_1 (integer)|
	+===================+
	|                 97|
	+-------------------+
	

ATAN
====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT ATAN(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "ATAN_1" : {
	      "script" : {
	        "source" : "def atan_1 = Math.atan(10);return atan_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|     ATAN_1 (double)|
	+====================+
	|  1.4711276743037347|
	+--------------------+
	

ATAN2
=====

Syntax: [(NUMBER T, NUMBER) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT ATAN2(10, 10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "ATAN2_1" : {
	      "script" : {
	        "source" : "def atan2_1 = Math.atan2(10, 10);return atan2_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|    ATAN2_1 (double)|
	+====================+
	|  0.7853981633974483|
	+--------------------+
	

CBRT
====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT CBRT(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "CBRT_1" : {
	      "script" : {
	        "source" : "def cbrt_1 = Math.cbrt(10);return cbrt_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+-------------------+
	|    CBRT_1 (double)|
	+===================+
	|  2.154434690031884|
	+-------------------+
	

CEIL
====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT CEIL(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "CEIL_1" : {
	      "script" : {
	        "source" : "def ceil_1 = Math.ceil(10);return ceil_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+-----------------+
	|  CEIL_1 (double)|
	+=================+
	|               10|
	+-----------------+
	

CONCAT
======

Syntax: []

Examples
--------

CONCAT_WS
=========

Syntax: []

Examples
--------

COS
===

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT COS(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "COS_1" : {
	      "script" : {
	        "source" : "def cos_1 = Math.cos(10);return cos_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+---------------------+
	|       COS_1 (double)|
	+=====================+
	|  -0.8390715290764524|
	+---------------------+
	

COSH
====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT COSH(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "COSH_1" : {
	      "script" : {
	        "source" : "def cosh_1 = Math.cosh(10);return cosh_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|     COSH_1 (double)|
	+====================+
	|  11013.232920103324|
	+--------------------+
	

COT
===

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT COT(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "COT_1" : {
	      "script" : {
	        "source" : "def cot_1 = 1 / Math.tan(10);return cot_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|      COT_1 (double)|
	+====================+
	|  1.5423510453569202|
	+--------------------+
	

DATE_FORMAT
===========

Syntax: []

Examples
--------

DEGREES
=======

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT DEGREES(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "DEGREES_1" : {
	      "script" : {
	        "source" : "def degrees_1 = Math.toDegrees(10);return degrees_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|  DEGREES_1 (double)|
	+====================+
	|   572.9577951308232|
	+--------------------+
	

E
=

Syntax: [() -> DOUBLE]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT E() FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "E_1" : {
	      "script" : {
	        "source" : "def E_2 = Math.E;return E_2;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+-------------------+
	|       E_1 (double)|
	+===================+
	|  2.718281828459045|
	+-------------------+
	

EXP
===

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT EXP(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "EXP_1" : {
	      "script" : {
	        "source" : "def exp_1 = Math.exp(10);return exp_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|      EXP_1 (double)|
	+====================+
	|  22026.465794806718|
	+--------------------+
	

EXPM1
=====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT EXPM1(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "EXPM1_1" : {
	      "script" : {
	        "source" : "def expm1_1 = Math.expm1(10);return expm1_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|    EXPM1_1 (double)|
	+====================+
	|  22025.465794806718|
	+--------------------+
	

FLOOR
=====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT FLOOR(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "FLOOR_1" : {
	      "script" : {
	        "source" : "def floor_1 = Math.floor(10);return floor_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+------------------+
	|  FLOOR_1 (double)|
	+==================+
	|                10|
	+------------------+
	

LENGTH
======

Syntax: [(STRING) -> INTEGER]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LENGTH('abc') FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LENGTH_1" : {
	      "script" : {
	        "source" : "def length_1 = 'abc'.length();return length_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|  LENGTH_1 (integer)|
	+====================+
	|                   3|
	+--------------------+
	

LOCATE
======

Syntax: [(STRING, STRING, INTEGER) -> INTEGER, (STRING, STRING) -> INTEGER]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOCATE('abc', 'abc', 10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOCATE_1" : {
	      "script" : {
	        "source" : "def locate_1 = 'abc'.indexOf('abc',9)+1;return locate_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|  LOCATE_1 (integer)|
	+====================+
	|                   0|
	+--------------------+
	

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOCATE('abc', 'abc') FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOCATE_1" : {
	      "script" : {
	        "source" : "def locate_1 = 'abc'.indexOf('abc',0)+1;return locate_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|  LOCATE_1 (integer)|
	+====================+
	|                   1|
	+--------------------+
	

LOG
===

Syntax: [(NUMBER T) -> T, (NUMBER T, NUMBER) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOG(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOG_1" : {
	      "script" : {
	        "source" : "def log_1 = Math.log(10)/Math.log(Math.E);return log_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+-------------------+
	|     LOG_1 (double)|
	+===================+
	|  2.302585092994046|
	+-------------------+
	

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOG(10, 10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOG_1" : {
	      "script" : {
	        "source" : "def log_1 = Math.log(10)/Math.log(10);return log_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+----------------+
	|  LOG_1 (double)|
	+================+
	|               1|
	+----------------+
	

LOG2
====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOG2(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOG2_1" : {
	      "script" : {
	        "source" : "def log_1 = Math.log(10)/Math.log(2);return log_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|     LOG2_1 (double)|
	+====================+
	|  3.3219280948873626|
	+--------------------+
	

LOG10
=====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOG10(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOG10_1" : {
	      "script" : {
	        "source" : "def log_1 = Math.log(10)/Math.log(10);return log_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+------------------+
	|  LOG10_1 (double)|
	+==================+
	|                 1|
	+------------------+
	

LN
==

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LN(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LN_1" : {
	      "script" : {
	        "source" : "def log_1 = Math.log(10)/Math.log(Math.E);return log_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+-------------------+
	|      LN_1 (double)|
	+===================+
	|  2.302585092994046|
	+-------------------+
	

LOWER
=====

Syntax: []

Examples
--------

LTRIM
=====

Syntax: [(STRING T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LTRIM('abc') FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LTRIM_1" : {
	      "script" : {
	        "source" : "int pos=0;while(pos < 'abc'.length() && Character.isWhitespace('abc'.charAt(pos))) {pos ++;} def ltrim_1 = 'abc'.substring(pos, 'abc'.length());return ltrim_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+----------------+
	|  LTRIM_1 (text)|
	+================+
	|             abc|
	+----------------+
	

PI
==

Syntax: [() -> DOUBLE]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT PI() FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "PI_1" : {
	      "script" : {
	        "source" : "def PI_2 = Math.PI;return PI_2;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+-------------------+
	|      PI_1 (double)|
	+===================+
	|  3.141592653589793|
	+-------------------+
	

POW
===

Syntax: []

Examples
--------

POWER
=====

Syntax: []

Examples
--------

RADIANS
=======

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT RADIANS(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "RADIANS_1" : {
	      "script" : {
	        "source" : "def radians_1 = Math.toRadians(10);return radians_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+---------------------+
	|   RADIANS_1 (double)|
	+=====================+
	|  0.17453292519943295|
	+---------------------+
	

REPLACE
=======

Syntax: [(STRING T, STRING, STRING) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT REPLACE('abc', 'abc', 'abc') FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "REPLACE_1" : {
	      "script" : {
	        "source" : "def replace_1 = 'abc'.replace('abc','abc');return replace_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+------------------+
	|  REPLACE_1 (text)|
	+==================+
	|               abc|
	+------------------+
	

RINT
====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT RINT(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "RINT_1" : {
	      "script" : {
	        "source" : "def rint_1 = Math.rint(10);return rint_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+-----------------+
	|  RINT_1 (double)|
	+=================+
	|               10|
	+-----------------+
	

ROUND
=====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT ROUND(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "ROUND_1" : {
	      "script" : {
	        "source" : "def round_1 = Math.round(10);return round_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+------------------+
	|  ROUND_1 (double)|
	+==================+
	|                10|
	+------------------+
	

RTRIM
=====

Syntax: [(STRING T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT RTRIM('abc') FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "RTRIM_1" : {
	      "script" : {
	        "source" : "int pos='abc'.length()-1;while(pos >= 0 && Character.isWhitespace('abc'.charAt(pos))) {pos --;} def rtrim_1 = 'abc'.substring(0, pos+1);return rtrim_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+----------------+
	|  RTRIM_1 (text)|
	+================+
	|             abc|
	+----------------+
	

SIGN
====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SIGN(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SIGN_1" : {
	      "script" : {
	        "source" : "def signum_1 = Math.signum(10);return signum_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+-----------------+
	|  SIGN_1 (double)|
	+=================+
	|                1|
	+-----------------+
	

SIGNUM
======

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SIGNUM(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SIGNUM_1" : {
	      "script" : {
	        "source" : "def signum_1 = Math.signum(10);return signum_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+-------------------+
	|  SIGNUM_1 (double)|
	+===================+
	|                  1|
	+-------------------+
	

SIN
===

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SIN(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SIN_1" : {
	      "script" : {
	        "source" : "def sin_1 = Math.sin(10);return sin_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+---------------------+
	|       SIN_1 (double)|
	+=====================+
	|  -0.5440211108893698|
	+---------------------+
	

SINH
====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SINH(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SINH_1" : {
	      "script" : {
	        "source" : "def sinh_1 = Math.sinh(10);return sinh_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|     SINH_1 (double)|
	+====================+
	|  11013.232874703393|
	+--------------------+
	

SQRT
====

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SQRT(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SQRT_1" : {
	      "script" : {
	        "source" : "def sqrt_1 = Math.sqrt(10);return sqrt_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|     SQRT_1 (double)|
	+====================+
	|  3.1622776601683795|
	+--------------------+
	

SUBSTRING
=========

Syntax: [(STRING T, INTEGER, INTEGER) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SUBSTRING('abc', 10, 10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SUBSTRING_1" : {
	      "script" : {
	        "source" : "def substring_1 = 'abc'.substring(9, 3);return substring_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|  SUBSTRING_1 (text)|
	+====================+
	

TAN
===

Syntax: [(NUMBER T) -> T]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT TAN(10) FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "TAN_1" : {
	      "script" : {
	        "source" : "def tan_1 = Math.tan(10);return tan_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+--------------------+
	|      TAN_1 (double)|
	+====================+
	|  0.6483608274590866|
	+--------------------+
	

UPPER
=====

Syntax: []

Examples
--------

YEAR
====

Syntax: [(DATE) -> INTEGER]

Examples
--------

SQL query::
	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT YEAR('2019-11-09') FROM accounts LIMIT 1"
	}

Explain::
	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "YEAR_1" : {
	      "script" : {
	        "source" : "def year_1 = doc['2019-11-09'].value.year;return year_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::
	+---------------+
	|  YEAR_1 (text)|
	+===============+
	

