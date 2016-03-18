#!/usr/bin/perl
#Description
#  This script add some other classes to the already present ones. 
#
use strict;
use warnings;
use DBI;

my $dbh;
my @buildings=("Dalhousie","Qmb","Tower B.", "Scrymgeour B.","Peters B.","Carnelley B.", "Bonar Hall");
my @start_time;
my @end_time;
my $array_size;

#This function modify the data we already have in. Basically add 2 days to the current data, Had to do this cause we already have a class in the db happanening on 
# Monday. Through this script I create another class called Lab happanening on Wednesday. All it does is getting the data reformat the datetime values and push the 
# data back into the db in order to create new class instances
sub modify_day{
    #my $array_size=scalar @file_array;
    $array_size=scalar(@start_time);
    #print "Array size= $array_size\n";
for (my $i=0; $i<$array_size; $i++){
    print $start_time[$i], " ",$end_time[$i],"\n";
    my $substr_start=substr($start_time[$i],8,2);
    my $substr_end =substr($end_time[$i],8,2);

    $substr_start+= '3';
    $substr_end+= '3';

    if($substr_start >31 || $substr_end> 31){ #if the value is higher than 31 then we need to fix it. We still have to modify the month, but for now that's ok. It's 
        $substr_start='01';                   #quick enough to do through phpmyadmin
        $substr_end='01';
    }
        
    if (length($substr_start)<2 ){
        $substr_start= '0' . $substr_start;
    }
    if(length($substr_end)<2 ){
        
        $substr_end= '0' . $substr_end;
    }
    substr($start_time[$i], 8, 2, $substr_start);
    substr($end_time[$i], 8, 2, $substr_end);
    #print $start_time[$i], " ", $end_time[$i],"\n";
   }
}

sub insert_data{
    
    my $classid=31;
    my $moduleid=10014;
    my $roomid="2G14";
    my $week=1;
     
    for(my $i=0; $i<$array_size; $i++){

$dbh->do("INSERT INTO android_class (id,week,class_type,qrCode,start_time,room_id,end_time,building,module_id) VALUES (?,?,?,?,?,?,?,?,?)",undef, $classid, $week, "Tutorial", 0, $start_time[$i],$roomid, $end_time[$i], $buildings[0], $moduleid);
    print $i,"\n";
$classid++;
$week++;
    }
} 

sub get_time{
my $i=1;
while( $i<16){
my $sth =$dbh->prepare("SELECT * FROM android_class WHERE id=$i") ;
$sth->execute();
    my $ref = $sth->fetchrow_hashref();
    push(@start_time, $ref->{'start_time'});
    push(@end_time, $ref->{'end_time'});
    #print $ref->{'start_time'},"\n";
$sth->finish();
$i++;
}    
}


#This script has the only purpose to ad some other classed based on the current ones.
sub main {
# Connect to the database.
$dbh = DBI->connect("DBI:mysql:database=Timetabling;host=5.39.43.115","root", "gUk2yuWSmBfk7SWT",{'RaiseError' => 1});
#Select data which is already in the db. 
get_time();
#Organize the data a bit
modify_day();
#insert new data into db
insert_data();

# Disconnect from the database.
$dbh->disconnect();

}

main();
