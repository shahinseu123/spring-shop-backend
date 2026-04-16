package com.shop.shop.supplier.entity;

import com.shop.shop.category.entity.Category;
import com.shop.shop.product.entity.Product;
import com.shop.shop.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false") // Soft delete filter
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Information
    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String shortName; // Abbreviation or short code

    @Column(unique = true, length = 100)
    private String supplierCode; // Unique code for supplier (e.g., SUP-001)

    @Column(length = 100)
    private String taxId; // Tax Identification Number / VAT Number

    @Column(length = 100)
    private String registrationNumber; // Business registration number

    // Contact Information
    @Column(length = 100)
    private String contactPerson;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 20)
    private String mobile;

    @Column(length = 20)
    private String fax;

    @Column(length = 150)
    private String website;

    @Column(length = 20)
    private String whatsappNumber;

    @Column(length = 20)
    private String emergencyContact;

    // Address Information
    @Column(length = 255)
    private String addressLine1;

    @Column(length = 255)
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String zipCode;

    @Column(length = 50)
    private String timezone;

    @Column(length = 50)
    private String language;

    // Bank Information
    @Column(length = 100)
    private String bankName;

    @Column(length = 50)
    private String bankAccountNumber;

    @Column(length = 50)
    private String bankAccountName;

    @Column(length = 20)
    private String bankRoutingNumber;

    @Column(length = 20)
    private String bankSwiftCode;

    @Column(length = 100)
    private String bankBranch;

    @Column(length = 100)
    private String iban; // International Bank Account Number

    @Column(length = 50)
    private String paymentTerms; // e.g., NET30, NET60, COD

    @Column(precision = 10, scale = 2)
    private BigDecimal creditLimit; // Maximum credit allowed

    @Column(precision = 10, scale = 2)
    private BigDecimal currentBalance; // Current outstanding balance

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPurchased; // Total purchase amount from this supplier

    private Integer creditDays; // Credit period in days

    // Supplier Performance Metrics
    @Column(length = 20)
    private String rating; // A+, A, B+, B, C, D

    private Double averageRating; // Numeric rating 0-5

    @Column(length = 20)
    private String performanceLevel; // EXCELLENT, GOOD, AVERAGE, POOR

    private Double onTimeDeliveryRate; // Percentage 0-100

    private Double qualityScore; // Percentage 0-100

    private Double priceCompetitiveness; // Percentage 0-100

    private Double communicationScore; // Percentage 0-100

    private Integer lateDeliveriesCount;

    private Integer earlyDeliveriesCount;

    private Integer onTimeDeliveriesCount;

    private Integer totalOrdersCount;

    private Integer returnedItemsCount;

    private Integer damagedItemsCount;

    // Business Categories
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "supplier_categories",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    private List<Category> categories = new ArrayList<>();

    @Column(length = 500)
    private String productTypes; // Comma-separated list of product types

    @Column(length = 100)
    private String industry; // Industry type (e.g., Electronics, Clothing, etc.)

    // Supplier Documents
    @ElementCollection
    @CollectionTable(name = "supplier_documents", joinColumns = @JoinColumn(name = "supplier_id"))
    @Column(name = "document_url")
    @Builder.Default
    private List<String> documentUrls = new ArrayList<>();

    @Column(length = 255)
    private String taxCertificateUrl;

    @Column(length = 255)
    private String tradeLicenseUrl;

    @Column(length = 255)
    private String bankStatementUrl;

    @Column(length = 255)
    private String ndaUrl; // Non-Disclosure Agreement

    @Column(length = 255)
    private String contractUrl;

    private LocalDateTime contractStartDate;

    private LocalDateTime contractEndDate;

    @Column(length = 1000)
    private String contractTerms;

    // Payment and Account Information
    @Column(length = 20)
    private String paymentMethod; // BANK_TRANSFER, CHEQUE, CASH, PAYPAL

    @Column(length = 50)
    private String currency; // USD, EUR, BDT, etc.

    @Column(precision = 10, scale = 2)
    private BigDecimal minimumOrderAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(precision = 10, scale = 2)
    private BigDecimal taxRate; // Tax rate applicable for this supplier

    @Column(length = 20)
    private String taxType; // VAT, GST, SALES_TAX, etc.

    @Column(length = 100)
    private String accountManager; // Person responsible for this supplier

    @Column(length = 100)
    private String accountManagerEmail;

    @Column(length = 20)
    private String accountManagerPhone;

    // Supplier Status
    @Column(nullable = false)
    private Boolean isActive = true;

    private Boolean isPreferred = false; // Preferred supplier

    private Boolean isApproved = false;

    private Boolean isVerified = false;

    @Column(length = 20)
    private String approvalStatus; // PENDING, APPROVED, REJECTED, SUSPENDED

    @Column(length = 500)
    private String rejectionReason;

    @Column(length = 20)
    private String supplierType; // LOCAL, INTERNATIONAL, MANUFACTURER, DISTRIBUTOR, WHOLESALER

    @Column(length = 20)
    private String riskLevel; // LOW, MEDIUM, HIGH

    @Column(length = 500)
    private String notes;

    @Column(length = 1000)
    private String internalNotes; // Internal staff-only notes

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @Builder.Default
//    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    // Timestamps
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime approvedAt;

    private LocalDateTime lastOrderDate;

    private LocalDateTime lastPaymentDate;

    // Soft delete
    private Boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User deletedBy;

    // Version for optimistic locking
    @Version
    private Long version;

    // Helper Methods
    public void updatePerformanceMetrics() {
        if (totalOrdersCount != null && totalOrdersCount > 0) {
            // Calculate on-time delivery rate
            if (onTimeDeliveriesCount != null) {
                this.onTimeDeliveryRate = (double) onTimeDeliveriesCount / totalOrdersCount * 100;
            }

            // Calculate quality score (assuming 100% - (return rate + damage rate))
            if (returnedItemsCount != null && damagedItemsCount != null) {
                int totalIssues = returnedItemsCount + damagedItemsCount;
                this.qualityScore = Math.max(0, 100 - ((double) totalIssues / totalOrdersCount * 100));
            }
        }

        // Calculate average rating
        if (qualityScore != null && onTimeDeliveryRate != null && priceCompetitiveness != null && communicationScore != null) {
            this.averageRating = (qualityScore + onTimeDeliveryRate + priceCompetitiveness + communicationScore) / 400 * 5;

            // Determine rating based on average rating
            if (averageRating >= 4.5) this.rating = "A+";
            else if (averageRating >= 4.0) this.rating = "A";
            else if (averageRating >= 3.5) this.rating = "B+";
            else if (averageRating >= 3.0) this.rating = "B";
            else if (averageRating >= 2.0) this.rating = "C";
            else this.rating = "D";
        }
    }

    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        if (addressLine1 != null) address.append(addressLine1);
        if (addressLine2 != null) address.append(", ").append(addressLine2);
        if (city != null) address.append(", ").append(city);
        if (state != null) address.append(", ").append(state);
        if (zipCode != null) address.append(" - ").append(zipCode);
        if (country != null) address.append(", ").append(country);
        return address.toString();
    }
}
