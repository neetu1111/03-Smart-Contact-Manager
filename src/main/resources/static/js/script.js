const search=()=>{

console.log("search.....");

let query=$("#search-input").val();

if(query=="")
{
$(".search-result").hide();
}
else{

console.log(query);

let url=`http://localhost:9090/search/${query}`;

fetch(url).then(response=>{

return response.json();
}).then((data) => {

    console.log(data);

    let text=`<div class='list-group'>`

    data.forEach((contact) => {

        text+=`<a href="#" class="list-group-item list-group-action">${contact.name}</a>`
        
    });

    text+=`</div>`

  $(".search-result").html(text);
  $(".search-result").show();



});
}
};


const paymentStart = () => {
console.log("payment start....");
var amount=$("#payment_field").val();
console.log(amount);

if(amount=="" || amount==null)
{
    alert("amount is required!!!");
    return;
}


$.ajax(
{

 url:'/user/create_order',
 data:JSON.stringify({amount:amount,info:'order_request'}),
 contentType:'application/json',
 type:'Post',
 dataType:'json',
 success:function(response)
 {
    console.log(response);
    if(response.status=="created"){

        let option={
            key:'rzp_test_wiMdjGDSgLmGpJ',
            amount:response.amount,
            currency:"INR",
            name:"smart contact manager",
            description:"Donation",
            Image:"https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png",
            order_id:response.id,

            handler:function(response){

                console.log(response.razorpay_payment_id);
                console.log(response.razorpay_order_id);
                console.log(response.razorpay_signature);
               console.log("payment successfull");
               alert("congrats !!!")

            },
            prefill: 
            { "name": "Gaurav Kumar", "email": "gaurav.kumar@example.com", "contact": "9999999999" }, 
            notes: { "address": "nandni Tech"
}, 

theme: { "color": "#3399cc" }

        };
        let rzp=new Razorpay(option);
        rzp.on('payment.failed', function (response)
        { alert(response.error.code);
            console.log(response.error.description); 
            console.log(response.error.source);
            console.log(response.error.step);
            console.log(response.error.reason); 
            console.log(response.error.metadata.order_id); 
            console.log(response.error.metadata.payment_id); 
          alert("opps payment fail !!!")
        });
        rzp.open();

    }
 },
 error:function(error)
 {
     alert("something went wrong!!!");
 },


})

};




















