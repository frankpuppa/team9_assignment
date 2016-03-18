#!/usr/bin/perl
#Description
#This script has been used to generate a list of student attendance.
use strict;
use warnings;
use DBI;
use experimental 'smartmatch';

my @classesids;
my @students_enrolled;
my $moduleid=10012;

# Connect to the database.
my $dbh = DBI->connect("DBI:mysql:database=Timetabling;host=5.39.43.115","root", "gUk2yuWSmBfk7SWT",{'RaiseError' => 1});


sub select_all_classes_for_module{
my $sth =$dbh->prepare("SELECT * FROM android_class where module_id=$moduleid") ;
$sth->execute();
while (my $ref = $sth->fetchrow_hashref()) {
    #print "Found a row:id = $ref->{'matric_number'}, first_name = $ref->{'first_name'}, last_name = $ref->{'last_name'}\n";
    push(@classesids, $ref->{'id'}); #store all matric_numbers in an array
}
$sth->finish();
}

sub select_students_enrolled_to_modules{
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


sub enroll_student_toclass{
    my $i=0;
    my $classesids=scalar(@classesids);
    my $total_students_enrolled=scalar(@students_enrolled);

        foreach my $class (@classesids){
            my $rand = 0 + int(rand($total_students_enrolled - 0)); #generate a number of students that will attend the module
            my $start_counting= 0 + int(rand($total_students_enrolled - 0));    #generate a number from which fo start inserting students
            #print $rand,"\n";
            for(my $i=0; $i<$rand; $i++){
                    if($start_counting > $#students_enrolled){      #if the counter pass the array size set it to 0
                        $start_counting=0;
                    }
                    #print "student will attend: $rand counter: $start_counting ";
                    #print "class: $class, student: $students_enrolled[$start_counting]\n";
                #insert data into db table
                $dbh->do("INSERT INTO android_class_class_register (class_id, student_id) VALUES (?,?)",undef, $class, $students_enrolled[$start_counting] ); 
            $start_counting++;
          }
    }
}

select_all_classes_for_module();
select_students_enrolled_to_modules();
enroll_student_toclass();
#print "@classesids\n";
#print "\n\n";
#print "@students_enrolled\n";


#select_students_enrolled();
#enroll_student_to_class();

# Disconnect from the database.
$dbh->disconnect();
