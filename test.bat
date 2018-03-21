
wc.exe -w -l -c test\test1.c

wc.exe -w -l -c test\test2.c

wc.exe -w -l -c -a test\test3.c

wc.exe -w -l -c test\test4.c -e test\stopList.txt
wc.exe -w -l -c -a test\test4.c -o output1.txt
wc.exe -w -l -c -a test\test4.c -e test\stopList.txt -o output2.txt
wc.exe -w -l -c -a -s test\*.c -e test\stopList.txt -o output3.txt
