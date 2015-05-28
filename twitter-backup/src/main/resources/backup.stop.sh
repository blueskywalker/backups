#!/bin/bash

jps | grep BackupTool | awk '{print $1}' | xargs kill -SIGTERM