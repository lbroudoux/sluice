![logo](https://raw.github.com/lbroudoux/sluice/master/admin-ui/app/images/sluice-logo.png)

Sluice
======

Elasticsearch plugin and console for easily managing and setting up Rivers.

Versions
--------

<table>
   <thead>
      <tr>
         <td>Sluice Plugin</td>
         <td>ElasticSearch</td>
      </tr>
   </thead>
   <tbody>
      <tr>
         <td>master (0.0.1-SNAPSHOT)</td>
         <td>1.3.x</td>
      </tr>
   </tbody>
</table>

Build Status
------------

Travis CI [![Build Status](https://travis-ci.org/lbroudoux/sluice.png?branch=master)](https://travis-ci.org/lbroudoux/sluice)


Getting Started
===============

Installation
------------

Just install as a regular Elasticsearch plugin by typing :

```sh
$ bin/plugin --install com.github.lbroudoux.elasticsearch/sluice/0.0.1
```

This will do the job...

```
-> Installing com.github.lbroudoux.elasticsearch/sluice/0.0.1...
Trying http://download.elasticsearch.org/com.github.lbroudoux.elasticsearch/sluice/sluice-1.2.0.zip...
Trying http://search.maven.org/remotecontent?filepath=com/github/lbroudoux/elasticsearch/sluice/0.0.1/sluice-0.0.1.zip...
Downloading ......DONE
Installed sluice
```

Usage / features
----------------

Open your favorite browser and point to `http://localhost:9200/_plugins/sluice` you should see the Sluice dashboard displaying.

Sluice console allows you to :
* Check installed River plugins among supported ones,
* Install River plugins not already installed onto your ES instance,
* Create and update new Rivers of supported types.

For now, supported River plugins are :
* [Amazon S3 River plugin](http://www.github.com/lbroudoux/es-amazon-s3-river),
* [Google Drive River plugin](http://www.github.com/lbroudoux/es-google-drive-river)


Known limitations
-----------------

Coming soon...


Advanced
========

Sluice endpoints
----------------

Sluice plugins enable different endpoints onto an Elasticsearch instance. See them in action...

```sh
$ curl -XGET 'http://localhost:9200/_sluice/river'
[{'name':'amazon-s3-river', 'installed':true}, {'name':'google-drive-river', 'installed':true}]
```

allows you to retrieve the list of supported and installed river plugins on your instance.

```sh
$ curl -XPOST 'http://localhost:9200/_sluice/river/google-drive-river'
{'installation_success':true}
```

allows you to dynamically install the Google Drive River plugin using the default version for this ES release.

```sh
$ curl -XPOST 'http://localhost:9200/_sluice/river/google-drive-river/1.3.0'
{'installation_success':true}
```

allows you to dynamically install the `1.3.0` version of Google Drive River plugin.
