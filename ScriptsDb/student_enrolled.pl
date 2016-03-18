#!/usr/bin/perl
#description
#This script has been used to enroll each student to three modules randomly picked.
use strict;
use warnings;
use DBI;
use experimental 'smartmatch';

my @matric_numbers;
my @module_ids;

# Connect to the database.
my $dbh = DBI->connect("DBI:mysql:database=Timetabling;host=5.39.43.115","root", "gUk2yuWSmBfk7SWT",{'RaiseError' => 1});

sub select_student{
my $sth =$dbh->prepare("SELECT * FROM android_student") ;
$sth->execute();
while (my $ref = $sth->fetchrow_hashref()) {
    #print "Found a row:id = $ref->{'matric_number'}, first_name = $ref->{'first_name'}, last_name = $ref->{'last_name'}\n";
    push(@matric_numbers, $ref->{'matric_number'});
}
$sth->finish();
}

sub select_modules{
my $sth =$dbh->prepare("SELECT * FROM android_module") ;
$sth->execute();
while (my $ref = $sth->fetchrow_hashref()) {
    #print "Found a row:id = $ref->{'matric_number'}, first_name = $ref->{'first_name'}, last_name = $ref->{'last_name'}\n";
    push(@module_ids, $ref->{'moduleid'});
}
$sth->finish();
}


sub enroll_student{
    my $student_total=scalar(@matric_numbers);
    my @holder;
    for(my $i=0; $i<$student_total; $i++){
        for(my $j=0; $j<3; $j++){
         my $rand = 0 + int(rand($#module_ids+1 - 0)); #generate a random number between 0 and total number of modules
        #my $rand = $module_ids[0] + int(rand($module_ids[0]+21 - $module_ids[0]));
            while( $rand ~~ @holder){ #if the array contains already a the same number, pick another one, it means that student has already been enrolled to that module.
                $rand = 0 + int(rand($#module_ids+1 - 0));
                #$rand = $module_ids[21] + int(rand($module_ids[21]+20 - $module_ids[21]));
            }
            push(@holder, $rand); #store number in array
            print $rand, " ", $matric_numbers[$i]," \n";  
             $dbh->do("INSERT INTO android_module_students_enrolled (module_id,student_id) VALUES (?,?)",undef, $module_ids[$rand], $matric_numbers[$i] );
        }
        undef(@holder); #at the end of the loop clear the array, as we need only 3 entry for each students.
    }
}

#select all students
select_student();
#select all modules
select_modules();
#each student is enrolled to 3 modules.
enroll_student();

# Disconnect from the database.
$dbh->disconnect();
