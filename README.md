# JAXB custom binding files generator for Plesk XML API 

This project generates custom binding declarations for [Plesk XML API](https://docs.plesk.com/en-US/obsidian/api-rpc/about-xml-api.28709/) schema files. 
The Plesk XML API schema files contains some conflicts and JAXB schema compilers generate some not so clear property names.

** note: this project does not contain the Plesk XML API schema files so you need to download them from your plesk server or from [Plesk XML-RPC API Schemes](https://plesk.github.io/api-schemas/) **

## How to use

Place the Plesk XML API schema files in a folder and run:

```shell
java -jar plesk-xml-api-generate-jaxbindings [folder]
```

For example:

```shell
java -jar plesk-xml-api-generate-jaxbindings C:\Plesk\schemas
```

The tool generates a xjb file per [XML API Operator](https://docs.plesk.com/en-US/obsidian/api-rpc/about-xml-api/reference/xml-schemas-for-xml-api-operators.58138/), agent_input and agent_output, which can be used with a JAXB schema compiler.
