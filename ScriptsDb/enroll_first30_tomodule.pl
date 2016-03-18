#!/usr/bin/perl
#Description
#This script has been used to enroll 30 students to a specific module.
use strict;
use warnings;
use DBI;
use experimental 'smartmatch';

my @matric_numbers;
my @students_enrolled;
my $moduleid=10012;

# Connect to the database.
my $dbh = DBI->connect("DBI:mysql:database=Timetabling;host=5.39.43.115","root", "gUk2yuWSmBfk7SWT",{'RaiseError' => 1});


sub select_student{
my $sth =$dbh->prepare("SELECT * FROM android_student") ;
$sth->execute();
while (my $ref = $sth->fetchrow_hashref()) {
    #print "Found a row:id = $ref->{'matric_number'}, first_name = $ref->{'first_name'}, last_name = $ref->{'last_name'}\n";
    push(@matric_numbers, $ref->{'matric_number'}); #store all matric_numbers in an array
}
$sth->finish();
}

sub select_students_enrolled{
my $sth =$dbh->prepare("SELECT * FROM android_module_students_enrolled WHERE module_id=$moduleid") ;
$sth->execute();
while (my $ref = $sth->fetchrow_hashref()) {
    #print "Found a row:id = $ref->{'matric_number'}, first_name = $ref->{'first_name'}, last_name = $ref->{'last_name'}\n";
    push(@students_enrolled, $ref->{'student_id'}); #store all students enrolled to a module into an array
}
$sth->finish();
#print @students_enrolled;
#print "\n";
}


sub enroll_student{
    my $i=0;
    my $student_total=scalar(@matric_numbers);
    #for(my $i=0; $i<$student_total; $i++){
        foreach(@matric_numbers){
            if($i>30){
                last;
            }
            #if( $_ ~~ @students_enrolled){ #if student is already enrolled, skip (done this cause the db already contained some data)
            #    next;
            # }
            print $_, " " ,$i, " \n";  
              $dbh->do("INSERT INTO android_module_students_enrolled (module_id,student_id) VALUES (?,?)",undef, $moduleid, $_ ); #insert data into db table
        $i++;
         }
}

select_student();
#print "@matric_numbers\n";
#select_students_enrolled();
enroll_student();

# Disconnect from the database.
$dbh->disconnect();
