#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUF_SIZE 512

char *
read_msg_alloc (char * msg, FILE * fp)
{
    msg = 0x0 ;

    char buf[BUF_SIZE] = "" ;
    int s = 0, len = 0 ;
    while((s = fread(buf, 1, BUF_SIZE, fp)) > 0) {
        buf[s] = 0x0 ;
        if (msg == 0x0) {
            msg = strdup(buf) ;
            len = s ;
        }
        else {
            msg = realloc(msg, len + s + 1) ;
            strncpy(msg + len, buf, s) ;
            msg[len + s] = 0x0 ;
            len += s ;
        }
    }

    return msg ;
}

int
main (int argc, char ** argv)
{
    if (argc < 1) {
        perror("usage: ./non-ascii-counter [filename]") ;
        exit(1) ;
    }

    char filename[2048] = "" ;
    strcpy(filename, argv[1]) ;

    FILE * fp = fopen(filename, "r") ;
    if (fp == 0x0) {
        perror("file does not exist") ;
        exit(1) ;
    }

    printf("# of consecutive non-ascii chars\n") ;
    char ch ;
    int non_ascii_cnt = 0 ;
    int non_ascii_exist = 0 ;
    while((ch = fgetc(fp)) != EOF) {
        if (ch < 0 || ch > 127) {
            non_ascii_cnt++ ;
            non_ascii_exist = 1 ;
        }
        else if (non_ascii_exist) {
            printf("%d, ", non_ascii_cnt) ;
            non_ascii_cnt = 0 ;
            non_ascii_exist = 0 ;
        }
    } 
    printf("\n") ;

    fclose(fp) ;
    return 0 ;
}